
import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import axios from 'axios';
import type { CardResponse, CommonResponse, UpdateCardRequest, CardRequest } from '../../types/card';

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
            // Check if it's an axios error with a response
            if (err.response && err.response.data) {
                // User says message is in 'data' field: { code: 400, data: "Message" }
                const serverMessage = err.response.data.data || err.response.data.status;
                return rejectWithValue(serverMessage || 'Error fetching cards');
            }
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
            if (err.response && err.response.data) {
                const serverMessage = err.response.data.data || err.response.data.status;
                return rejectWithValue(serverMessage || 'Error updating card');
            }
            return rejectWithValue(err.message || 'Error updating card');
        }
    }
);

// ... imports

export const createCard = createAsyncThunk(
    'cards/createCard',
    async (cardData: CardRequest, { rejectWithValue, dispatch }) => {
        try {
            const response = await axios.post<CommonResponse<string>>('/cards', cardData);
            if (response.data.code === 201) { // Assuming 201 Created
                dispatch(fetchCards());
                return response.data.message;
            } else {
                return rejectWithValue(response.data.status || 'Creation failed');
            }
        } catch (err: any) {
            if (err.response && err.response.data) {
                const serverMessage = err.response.data.data || err.response.data.status;
                return rejectWithValue(serverMessage || 'Error creating card');
            }
            return rejectWithValue(err.message || 'Error creating card');
        }
    }
);

export const deleteCard = createAsyncThunk(
    'cards/deleteCard',
    async (encryptedCardNumber: string, { rejectWithValue, dispatch }) => {
        try {
            const response = await axios.delete<CommonResponse<string>>(`/cards/${encryptedCardNumber}`);
            if (response.data.code === 200) {
                dispatch(fetchCards());
                return response.data.message;
            } else {
                return rejectWithValue(response.data.status || 'Deletion failed');
            }
        } catch (err: any) {
            if (err.response && err.response.data) {
                const serverMessage = err.response.data.data || err.response.data.status;
                return rejectWithValue(serverMessage || 'Error deleting card');
            }
            return rejectWithValue(err.message || 'Error deleting card');
        }
    }
);

// ... updateCard thunk ...

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
                // Only show global loading spinner if we have no data
                if (state.cards.length === 0) {
                    state.loading = true;
                }
                state.error = null;
            })
            .addCase(fetchCards.fulfilled, (state, action) => {
                state.loading = false;
                state.cards = action.payload;
            })
            .addCase(fetchCards.rejected, (state, action) => {
                state.loading = false;
                // Only set global error if we have no data to show
                if (state.cards.length === 0) {
                    state.error = action.payload as string;
                }
                // If we have data, we might want to show a toast instead, 
                // but the thunk doesn't dispatch a toast. 
                // For now, keeping the table visible is the priority.
            });
        // We removed Create/Update/Delete handlers because they are handled locally
        // via unwrap() and toasts, and we don't want them to trigger global loading/error states
        // that would hide the table.
    },
});

export const { clearError } = cardSlice.actions;
export default cardSlice.reducer;
