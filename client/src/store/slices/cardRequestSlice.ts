import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import axios from 'axios';
import { toast } from 'sonner';
import type { CardRequestData, CommonResponse } from '../../types/card';

interface CardRequestState {
    requests: CardRequestData[];
    loading: boolean;
    error: string | null;
}

const initialState: CardRequestState = {
    requests: [],
    loading: false,
    error: null,
};

// Async thunk for fetching card requests
export const fetchCardRequests = createAsyncThunk(
    'cardRequests/fetchCardRequests',
    async (_, { rejectWithValue }) => {
        try {
            const response = await axios.get<CommonResponse<CardRequestData[]>>('/card-requests');
            if (response.data.code === 200 && Array.isArray(response.data.data)) {
                // Return sorted by requestId descending
                return response.data.data.sort((a, b) => b.requestId - a.requestId);
            } else {
                return rejectWithValue(response.data.status || 'Failed to fetch card requests');
            }
        } catch (err: any) {
            return rejectWithValue(err.response?.data?.status || err.message || 'Error fetching card requests');
        }
    }
);

const cardRequestSlice = createSlice({
    name: 'cardRequests',
    initialState,
    reducers: {
        clearError: (state) => {
            state.error = null;
        }
    },
    extraReducers: (builder) => {
        builder
            .addCase(fetchCardRequests.pending, (state) => {
                // If we don't have requests yet, set full loading. Otherwise background load.
                if (state.requests.length === 0) {
                    state.loading = true;
                }
                state.error = null;
            })
            .addCase(fetchCardRequests.fulfilled, (state, action) => {
                state.loading = false;
                state.requests = action.payload;
            })
            .addCase(fetchCardRequests.rejected, (state, action) => {
                state.loading = false;
                const errorMessage = action.payload as string;
                state.error = errorMessage;

                // Toast if we already have data but background refresh fails
                if (state.requests.length > 0) {
                    toast.error(errorMessage);
                }
            });
    },
});

export const { clearError } = cardRequestSlice.actions;

export default cardRequestSlice.reducer;
