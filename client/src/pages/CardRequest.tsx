import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Loader2, Search } from 'lucide-react';
import type { CardResponse } from '../types/card';
import { fetchCards, clearError } from '../store/slices/cardSlice';
import { fetchStatusTypes } from '../store/slices/optionsSlice';
import type { RootState, AppDispatch } from '../store/index';
import CardTable from '../components/CardTable';
import SendRequestModal from '../components/SendRequestModal';

const CardRequest = () => {
    const dispatch = useDispatch<AppDispatch>();
    const { cards, loading, error } = useSelector((state: RootState) => state.cards);
    const { statusTypes } = useSelector((state: RootState) => state.options);

    const [selectedCard, setSelectedCard] = useState<CardResponse | null>(null);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [searchTerm, setSearchTerm] = useState('');
    const [statusFilter, setStatusFilter] = useState('');

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
        <div className="p-8 max-w-400 mx-auto">
            <div className="mb-8 flex justify-between items-end">
                <div>
                    <h1 className="text-3xl font-extrabold text-slate-900 tracking-tight">Card Requests</h1>
                    <p className="text-slate-500 mt-2 text-base">Select a card to send a new request.</p>
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
                <CardTable
                    cards={filteredCards}
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
