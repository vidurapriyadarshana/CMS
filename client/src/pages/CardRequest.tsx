import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Loader2 } from 'lucide-react';
import type { CardResponse } from '../types/card';
import { fetchCards, clearError } from '../store/slices/cardSlice';
import type { RootState, AppDispatch } from '../store/index';
import CardTable from '../components/CardTable';
import SendRequestModal from '../components/SendRequestModal';

const CardRequest = () => {
    const dispatch = useDispatch<AppDispatch>();
    const { cards, loading, error } = useSelector((state: RootState) => state.cards);

    const [selectedCard, setSelectedCard] = useState<CardResponse | null>(null);
    const [isModalOpen, setIsModalOpen] = useState(false);

    useEffect(() => {
        dispatch(fetchCards());
    }, [dispatch]);

    useEffect(() => {
        return () => {
            dispatch(clearError());
        };
    }, [dispatch]);

    const handleSendRequest = (card: CardResponse) => {
        setSelectedCard(card);
        setIsModalOpen(true);
    };

    return (
        <div className="p-8 max-w-[1600px] mx-auto">
            <div className="mb-8">
                <h1 className="text-3xl font-bold text-slate-900 tracking-tight">Card Requests</h1>
                <p className="text-slate-500 mt-2">Select a card to send a new request.</p>
            </div>

            {loading ? (
                <div className="flex h-96 items-center justify-center">
                    <Loader2 className="w-8 h-8 animate-spin text-blue-500" />
                </div>
            ) : error ? (
                <div className="bg-red-50 border border-red-200 rounded-xl p-8 text-center text-red-600 shadow-sm">
                    <p className="text-lg font-medium">{error}</p>
                    <button
                        onClick={() => dispatch(fetchCards())}
                        className="mt-6 px-6 py-2.5 bg-white border border-red-200 rounded-xl text-sm font-semibold hover:bg-red-50 transition-colors shadow-sm focus:outline-none focus:ring-2 focus:ring-red-500 focus:ring-offset-2"
                    >
                        Try Again
                    </button>
                </div>
            ) : (
                <CardTable
                    cards={cards}
                    onCardClick={handleSendRequest}
                    onRequest={handleSendRequest}
                />
            )}

            <SendRequestModal
                isOpen={isModalOpen}
                onClose={() => {
                    setIsModalOpen(false);
                    setSelectedCard(null);
                }}
                card={selectedCard}
            />
        </div>
    );
};

export default CardRequest;
