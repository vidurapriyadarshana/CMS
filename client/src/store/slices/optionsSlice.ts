import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import axios from 'axios';
import type { CardRequestType, StatusType, CommonResponse } from '../../types/card';

interface OptionsState {
    requestTypes: CardRequestType[];
    statusTypes: StatusType[];
    loadingRequestTypes: boolean;
    loadingStatusTypes: boolean;
    errorRequestTypes: string | null;
    errorStatusTypes: string | null;
}

const initialState: OptionsState = {
    requestTypes: [],
    statusTypes: [],
    loadingRequestTypes: false,
    loadingStatusTypes: false,
    errorRequestTypes: null,
    errorStatusTypes: null,
};

// Async thunk for fetching card request types
export const fetchRequestTypes = createAsyncThunk(
    'options/fetchRequestTypes',
    async (_, { rejectWithValue }) => {
        try {
            const response = await axios.get<CommonResponse<CardRequestType[]>>('/card-request-types');
            if (response.data.code === 200 && Array.isArray(response.data.data)) {
                return response.data.data;
            } else {
                return rejectWithValue(response.data.status || 'Failed to load request types');
            }
        } catch (err: any) {
            return rejectWithValue(err.response?.data?.status || err.message || 'Failed to load request types');
        }
    }
);

// Async thunk for fetching status types
export const fetchStatusTypes = createAsyncThunk(
    'options/fetchStatusTypes',
    async (_, { rejectWithValue }) => {
        try {
            const response = await axios.get<CommonResponse<StatusType[]>>('/status');
            if (response.data.code === 200 && Array.isArray(response.data.data)) {
                return response.data.data;
            } else {
                return rejectWithValue(response.data.status || 'Failed to load statuses');
            }
        } catch (err: any) {
            return rejectWithValue(err.response?.data?.status || err.message || 'Failed to load statuses');
        }
    }
);

const optionsSlice = createSlice({
    name: 'options',
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        // Request Types
        builder
            .addCase(fetchRequestTypes.pending, (state) => {
                state.loadingRequestTypes = true;
                state.errorRequestTypes = null;
            })
            .addCase(fetchRequestTypes.fulfilled, (state, action) => {
                state.loadingRequestTypes = false;
                state.requestTypes = action.payload;
            })
            .addCase(fetchRequestTypes.rejected, (state, action) => {
                state.loadingRequestTypes = false;
                state.errorRequestTypes = action.payload as string;
            });

        // Status Types
        builder
            .addCase(fetchStatusTypes.pending, (state) => {
                state.loadingStatusTypes = true;
                state.errorStatusTypes = null;
            })
            .addCase(fetchStatusTypes.fulfilled, (state, action) => {
                state.loadingStatusTypes = false;
                state.statusTypes = action.payload;
            })
            .addCase(fetchStatusTypes.rejected, (state, action) => {
                state.loadingStatusTypes = false;
                state.errorStatusTypes = action.payload as string;
            });
    },
});

export default optionsSlice.reducer;
