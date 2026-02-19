
import { createSlice, createAsyncThunk, type PayloadAction } from '@reduxjs/toolkit';
import axios from 'axios';
import type { CardResponse, CommonResponse, UpdateCardRequest } from '../../types/card';

interface CardState {
    cards: CardResponse[];
    loading: boolean;
    error: string | null;
}

const initialState: CardState = {
    cards: [],
    loading: false,
    error: null,
};

export const fetchCards = createAsyncThunk(
    'cards/fetchCards',
    async (_, { rejectWithValue }) => {
        try {
            const response = await axios.get<CommonResponse<CardResponse[]>>('/cards');
            if (response.data.code === 200 && Array.isArray(response.data.data)) {
                // Sort inside the thunk to keep reducer pure if possible, or just pass data.
                // Let's sort here for convenience.
                return response.data.data.sort((a, b) => {
                    const dateA = a.lastUpdateTime ? new Date(a.lastUpdateTime).getTime() : 0;
                    const dateB = b.lastUpdateTime ? new Date(b.lastUpdateTime).getTime() : 0;
                    return dateB - dateA;
                });
            } else {
                return rejectWithValue(response.data.status || 'Failed to fetch cards');
            }
        } catch (err: any) {
            return rejectWithValue(err.message || 'Error fetching cards');
        }
    }
);

export const updateCard = createAsyncThunk(
    'cards/updateCard',
    async ({ encryptedCardNumber, data }: { encryptedCardNumber: string; data: UpdateCardRequest }, { rejectWithValue, dispatch }) => {
        try {
            const response = await axios.put<CommonResponse<string>>(`/cards/${encryptedCardNumber}`, data);
            if (response.data.code === 200) {
                // We could return the data to update local state, or re-fetch.
                // Re-fetching ensures consistency.
                dispatch(fetchCards());
                return response.data.message;
            } else {
                return rejectWithValue(response.data.status || 'Update failed');
            }
        } catch (err: any) {
            return rejectWithValue(err.message || 'Error updating card');
        }
    }
);

const cardSlice = createSlice({
    name: 'cards',
    initialState,
    reducers: {
        clearError: (state) => {
            state.error = null;
        }
    },
    extraReducers: (builder) => {
        builder
            // Fetch Cards
            .addCase(fetchCards.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchCards.fulfilled, (state, action) => {
                state.loading = false;
                state.cards = action.payload;
            })
            .addCase(fetchCards.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload as string;
            })
            // Update Card
            .addCase(updateCard.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(updateCard.fulfilled, (state) => {
                state.loading = false;
                // Fetch is triggered by thunk, so we just stop loading here.
                // Ideally, if we want seamless UI, we might optimistically update or 
                // wait for fetch. Since we dispatch fetchCards inside updateCard, 
                // the fetchCards.pending will trigger immediately after this or concurrently.
                // Actually, dispatching inside thunk means a new action cycle starts.
            })
            .addCase(updateCard.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload as string;
            });
    },
});

export const { clearError } = cardSlice.actions;
export default cardSlice.reducer;
