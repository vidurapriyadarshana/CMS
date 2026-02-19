
import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import type { CardResponse, UpdateCardRequest } from '../types/card';
import { fetchCards, updateCard, clearError } from '../store/slices/cardSlice';
import type { RootState, AppDispatch } from '../store/index';
import CardTable from '../components/CardTable';
import CardDetailsModal from '../components/CardDetailsModal';
import { Loader2 } from 'lucide-react';

const ManageCards = () => {
    const dispatch = useDispatch<AppDispatch>();
    const { cards, loading, error } = useSelector((state: RootState) => state.cards);

    const [selectedCard, setSelectedCard] = useState<CardResponse | null>(null);
    const [isModalOpen, setIsModalOpen] = useState(false);

    useEffect(() => {
        dispatch(fetchCards());
    }, [dispatch]);

    // Clear error on unmount
    useEffect(() => {
        return () => {
            dispatch(clearError());
        };
    }, [dispatch]);

    const handleCardClick = (card: CardResponse) => {
        setSelectedCard(card);
        setIsModalOpen(true);
    };

    const handleUpdateCard = async (encryptedCardNumber: string, data: UpdateCardRequest) => {
        try {
            await dispatch(updateCard({ encryptedCardNumber, data })).unwrap();
            // No need to close modal here, user can close it manually or we can close it.
            // Success handling is done via thunk lifecycle, but unwrap throws on error.
            // If successful, we can show a success message or just let the updated list show.
        } catch (err) {
            console.error('Update failed', err);
            throw err; // Re-throw to let modal verify failure
        }
    };

    return (
        <div className="p-8 max-w-[1600px] mx-auto">
            <div className="mb-8 flex justify-between items-end">
                <div>
                    <h1 className="text-3xl font-bold text-slate-900 tracking-tight">Manage Cards</h1>
                    <p className="text-slate-500 mt-2">View and manage all customer credit cards.</p>
                </div>
                <div className="text-right">
                    <span className="text-sm font-medium text-slate-500">Total Cards</span>
                    <p className="text-2xl font-bold text-slate-900 leading-none">{cards.length}</p>
                </div>
            </div>

            {loading ? (
                <div className="flex h-96 items-center justify-center">
                    <Loader2 className="w-8 h-8 animate-spin text-slate-400" />
                </div>
            ) : error ? (
                <div className="bg-red-50 border border-red-200 rounded-xl p-6 text-center text-red-600">
                    <p>{error}</p>
                    <button
                        onClick={() => dispatch(fetchCards())}
                        className="mt-4 px-4 py-2 bg-white border border-red-200 rounded-lg text-sm font-medium hover:bg-red-50 transition-colors"
                    >
                        Try Again
                    </button>
                </div>
            ) : (
                <CardTable cards={cards} onCardClick={handleCardClick} />
            )}

            {selectedCard && (
                <CardDetailsModal
                    card={selectedCard}
                    isOpen={isModalOpen}
                    onClose={() => setIsModalOpen(false)}
                    onUpdate={handleUpdateCard}
                />
            )}
        </div>
    );
};

export default ManageCards;
