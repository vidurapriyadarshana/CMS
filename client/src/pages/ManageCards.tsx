
import { useEffect, useState } from 'react';
import axios from 'axios';
import type { CardResponse, CommonResponse, UpdateCardRequest } from '../types/card';
import CardTable from '../components/CardTable';
import CardDetailsModal from '../components/CardDetailsModal';
import { Loader2 } from 'lucide-react';

const ManageCards = () => {
    const [cards, setCards] = useState<CardResponse[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [selectedCard, setSelectedCard] = useState<CardResponse | null>(null);
    const [isModalOpen, setIsModalOpen] = useState(false);

    const fetchCards = async () => {
        try {
            setLoading(true);
            const response = await axios.get<CommonResponse<CardResponse[]>>('/cards');
            if (response.data.code === 200 && Array.isArray(response.data.data)) {
                // Sort by last update time descending (optional, but good UX)
                const sortedCards = response.data.data.sort((a, b) => {
                    const dateA = a.lastUpdateTime ? new Date(a.lastUpdateTime).getTime() : 0;
                    const dateB = b.lastUpdateTime ? new Date(b.lastUpdateTime).getTime() : 0;
                    return dateB - dateA;
                });
                setCards(sortedCards);
            } else if (response.data.code !== 200) {
                setError('Failed to fetch cards: ' + response.data.status);
            } else {
                setCards([]); // Fallback if data is not an array
            }
        } catch (err: any) {
            console.error('Error in fetchCards:', err);
            setError(err.message || 'An unexpected error occurred.');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchCards();
    }, []);

    const handleCardClick = (card: CardResponse) => {
        setSelectedCard(card);
        setIsModalOpen(true);
    };

    const handleUpdateCard = async (encryptedCardNumber: string, data: UpdateCardRequest) => {
        try {
            const response = await axios.put<CommonResponse<string>>(`/cards/${encryptedCardNumber}`, data);
            if (response.data.code === 200) {
                // Refresh list on success
                await fetchCards();
                // Update selected card in local state to reflect changes immediately if needed, 
                // but fetching fresh data is safer.
                // We might need to close modal or keep it open with updated data.
                // Let's keep it open but update the selected card ref if possible, 
                // but since we re-fetched, the `cards` array is new.
                // We'll just close it or let the user decide. 
                // For now, let's close it as per common pattern, or just toast success.
                // The Modal implementation calls onClose inside itself on success? 
                // No, the modal calls this function and waits. The modal closes itself after this promise resolves.
            } else {
                throw new Error('Update failed');
            }
        } catch (err) {
            console.error('Update card error:', err);
            throw err; // Propagate to modal to show error
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
                        onClick={fetchCards}
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
