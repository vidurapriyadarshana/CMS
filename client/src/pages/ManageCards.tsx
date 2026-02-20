
import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Plus } from 'lucide-react';
import { toast } from 'sonner';
import type { CardResponse, UpdateCardRequest, CardRequest } from '../types/card';
import { fetchCards, updateCard, createCard, deleteCard, clearError } from '../store/slices/cardSlice';
import type { RootState, AppDispatch } from '../store/index';
import CardTable from '../components/CardTable';
import CardDetailsModal from '../components/CardDetailsModal';
import CreateCardModal from '../components/CreateCardModal';
import DeleteConfirmationModal from '../components/DeleteConfirmationModal';
import { Loader2 } from 'lucide-react';

const ManageCards = () => {
    const dispatch = useDispatch<AppDispatch>();
    const { cards, loading, error } = useSelector((state: RootState) => state.cards);

    const [selectedCard, setSelectedCard] = useState<CardResponse | null>(null);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);

    // Delete state
    const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
    const [cardToDelete, setCardToDelete] = useState<string | null>(null);

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
            toast.success('Card updated successfully');
        } catch (err) {
            console.error('Update failed', err);
            toast.error(typeof err === 'string' ? err : 'Failed to update card');
            throw err;
        }
    };

    const handleCreateCard = async (data: CardRequest) => {
        try {
            await dispatch(createCard(data)).unwrap();
            toast.success('Card created successfully');
            setIsCreateModalOpen(false);
        } catch (err) {
            console.error('Create failed', err);
            toast.error(typeof err === 'string' ? err : 'Failed to create card');
            // Don't throw if we want modal to stay open on error, but CreateCardModal handles error too? 
            // If CreateCardModal handles it, we should throw. 
            throw err;
        }
    };

    const confirmDeleteCard = (encryptedCardNumber: string) => {
        setCardToDelete(encryptedCardNumber);
        setIsDeleteModalOpen(true);
    };

    const handleDeleteCard = async () => {
        if (!cardToDelete) return;
        try {
            await dispatch(deleteCard(cardToDelete)).unwrap();
            toast.success('Card deleted successfully');
            setIsDeleteModalOpen(false);
            setCardToDelete(null);
        } catch (err) {
            console.error('Delete failed', err);
            toast.error(typeof err === 'string' ? err : 'Failed to delete card');
        }
    };

    return (
        <div className="p-8 max-w-[1600px] mx-auto">
            <div className="mb-8 flex justify-between items-end">
                <div>
                    <h1 className="text-3xl font-bold text-slate-900 tracking-tight">Manage Cards</h1>
                    <p className="text-slate-500 mt-2">View and manage all customer credit cards.</p>
                </div>
                <div className="text-right flex items-center gap-4">
                    <button
                        onClick={() => setIsCreateModalOpen(true)}
                        className="flex items-center gap-2 px-4 py-2 bg-blue-600 text-white rounded-lg font-medium hover:bg-blue-700 transition-colors shadow-sm"
                    >
                        <Plus className="w-4 h-4" />
                        Create New Card
                    </button>
                    <div>
                        <span className="text-sm font-medium text-slate-500 block">Total Cards</span>
                        <p className="text-2xl font-bold text-slate-900 leading-none">{cards.length}</p>
                    </div>
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
                <CardTable cards={cards} onCardClick={handleCardClick} onDelete={confirmDeleteCard} />
            )}

            {selectedCard && (
                <CardDetailsModal
                    card={selectedCard}
                    isOpen={isModalOpen}
                    onClose={() => setIsModalOpen(false)}
                    onUpdate={handleUpdateCard}
                />
            )}

            <CreateCardModal
                isOpen={isCreateModalOpen}
                onClose={() => setIsCreateModalOpen(false)}
                onCreate={handleCreateCard}
            />

            <DeleteConfirmationModal
                isOpen={isDeleteModalOpen}
                onClose={() => setIsDeleteModalOpen(false)}
                onConfirm={handleDeleteCard}
            />
        </div>
    );
};

export default ManageCards;
