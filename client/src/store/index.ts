import { configureStore } from '@reduxjs/toolkit';
import cardReducer from './slices/cardSlice';
import optionsReducer from './slices/optionsSlice';
import cardRequestReducer from './slices/cardRequestSlice';

export const store = configureStore({
    reducer: {
        cards: cardReducer,
        options: optionsReducer,
        cardRequests: cardRequestReducer,
    },
})

// Infer the `RootState` and `AppDispatch` types from the store itself
export type RootState = ReturnType<typeof store.getState>
// Inferred type: {posts: PostsState, comments: CommentsState, users: UsersState}
export type AppDispatch = typeof store.dispatch
