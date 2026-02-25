
import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Plus, Search } from 'lucide-react';
import { toast } from 'sonner';
import type { CardResponse, UpdateCardRequest, CardRequest } from '../types/card';
import { fetchCards, updateCard, createCard, deleteCard, clearError } from '../store/slices/cardSlice';
import { fetchStatusTypes } from '../store/slices/optionsSlice';
import type { RootState, AppDispatch } from '../store/index';
import CardTable from '../components/CardTable';
import CardDetailsModal from '../components/CardDetailsModal';
import CreateCardModal from '../components/CreateCardModal';
import DeleteConfirmationModal from '../components/DeleteConfirmationModal';
import { Loader2 } from 'lucide-react';

const ManageCards = () => {
    const dispatch = useDispatch<AppDispatch>();
    const { cards, loading, error } = useSelector((state: RootState) => state.cards);
    const { statusTypes } = useSelector((state: RootState) => state.options);

    const [selectedCard, setSelectedCard] = useState<CardResponse | null>(null);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
    const [searchTerm, setSearchTerm] = useState('');
    const [statusFilter, setStatusFilter] = useState('');

    // Delete state
    const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
    const [cardToDelete, setCardToDelete] = useState<string | null>(null);

    const filteredCards = cards.filter(card => {
        if (!searchTerm) return true;
        const last4 = card.cardNumber.slice(-4);
        return last4.includes(searchTerm);
    });

    useEffect(() => {
        dispatch(fetchStatusTypes());
    }, [dispatch]);

    useEffect(() => {
        dispatch(fetchCards(statusFilter || undefined));
    }, [dispatch, statusFilter]);

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
            dispatch(fetchCards(statusFilter || undefined));
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
            dispatch(fetchCards(statusFilter || undefined));
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
            dispatch(fetchCards(statusFilter || undefined));
        } catch (err) {
            console.error('Delete failed', err);
            toast.error(typeof err === 'string' ? err : 'Failed to delete card');
        }
    };

    return (
        <div className="p-8 max-w-400 mx-auto">
            <div className="mb-8 flex justify-between items-end">
                <div>
                    <h1 className="text-3xl font-extrabold text-slate-900 tracking-tight">Manage Cards</h1>
                    <p className="text-slate-500 mt-2 text-base">View and manage all customer credit cards.</p>
                </div>
                <div className="text-right flex items-center gap-6">
                    <div className="flex gap-3">
                        <select
                            value={statusFilter}
                            onChange={(e) => setStatusFilter(e.target.value)}
                            className="bg-white border border-slate-200 rounded-xl text-sm font-medium focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 shadow-sm py-2.5 pl-3 pr-8 text-slate-700 cursor-pointer"
                        >
                            <option value="">All Statuses</option>
                            {statusTypes.map((status) => (
                                <option key={status.statusCode} value={status.statusCode}>
                                    {status.description}
                                </option>
                            ))}
                        </select>
                        <div className="relative">
                            <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                <Search className="h-4 w-4 text-slate-400" />
                            </div>
                            <input
                                type="text"
                                placeholder="Search last 4 digits..."
                                value={searchTerm}
                                onChange={(e) => setSearchTerm(e.target.value.replace(/\D/g, '').slice(0, 4))}
                                className="pl-10 pr-4 py-2.5 bg-white border border-slate-200 rounded-xl text-sm font-medium focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 shadow-sm w-48"
                            />
                        </div>
                    </div>
                    <button
                        onClick={() => setIsCreateModalOpen(true)}
                        className="flex items-center gap-2 px-5 py-2.5 bg-blue-600 hover:bg-blue-700 text-white rounded-xl font-semibold transition-colors shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 active:scale-95"
                    >
                        <Plus className="w-5 h-5" />
                        Create New Card
                    </button>
                    <div className="bg-white px-5 py-2.5 rounded-xl border border-slate-200 shadow-sm flex flex-col items-end">
                        <span className="text-[11px] font-bold text-slate-400 uppercase tracking-widest leading-none mb-1">Total Cards</span>
                        <p className="text-2xl font-black text-slate-800 leading-none">{cards.length}</p>
                    </div>
                </div>
            </div>

            {loading ? (
                <div className="flex h-96 items-center justify-center">
                    <Loader2 className="w-8 h-8 animate-spin text-blue-500" />
                </div>
            ) : error ? (
                <div className="bg-red-50 border border-red-200 rounded-xl p-8 text-center text-red-600 shadow-sm">
                    <p className="text-lg font-medium">{error}</p>
                    <button
                        onClick={() => dispatch(fetchCards(statusFilter || undefined))}
                        className="mt-6 px-6 py-2.5 bg-white border border-red-200 rounded-xl text-sm font-semibold hover:bg-red-50 transition-colors shadow-sm focus:outline-none focus:ring-2 focus:ring-red-500 focus:ring-offset-2"
                    >
                        Try Again
                    </button>
                </div>
            ) : (
                <CardTable cards={filteredCards} onCardClick={handleCardClick} onDelete={confirmDeleteCard} />
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
