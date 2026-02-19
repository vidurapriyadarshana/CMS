
import React, { useState, useEffect } from 'react';
import type { CardResponse, UpdateCardRequest } from '../types/card';
import CardVisual from './CardVisual';
import { X, Save, AlertCircle } from 'lucide-react';

interface CardDetailsModalProps {
    card: CardResponse;
    isOpen: boolean;
    onClose: () => void;
    onUpdate: (encryptedCardNumber: string, data: UpdateCardRequest) => Promise<void>;
}

const CardDetailsModal: React.FC<CardDetailsModalProps> = ({ card, isOpen, onClose, onUpdate }) => {
    const [formData, setFormData] = useState<UpdateCardRequest>({
        expireDate: '',
        creditLimit: 0,
        cashLimit: 0,
        availableCreditLimit: 0,
        availableCashLimit: 0
    });
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (card) {
            setFormData({
                expireDate: card.expireDate,
                creditLimit: card.creditLimit,
                cashLimit: card.cashLimit,
                availableCreditLimit: card.availableCreditLimit,
                availableCashLimit: card.availableCashLimit
            });
        }
    }, [card]);

    if (!isOpen) return null;

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: name === 'expireDate' ? value : Number(value)
        }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setError(null);
        try {
            await onUpdate(card.encryptedCardNumber, formData);
            onClose();
        } catch (err) {
            setError('Failed to update card details. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm p-4">
            <div className="bg-white rounded-2xl shadow-2xl w-full max-w-4xl overflow-hidden flex flex-col md:flex-row relative animate-in fade-in zoom-in-95 duration-200">

                {/* Close Button */}
                <button
                    onClick={onClose}
                    className="absolute top-4 right-4 p-2 rounded-full hover:bg-slate-100 text-slate-500 transition-colors z-20"
                >
                    <X className="w-5 h-5" />
                </button>

                {/* Left Side - Visual & Status */}
                <div className="w-full md:w-5/12 bg-slate-50 p-8 flex flex-col items-center justify-center border-r border-slate-100">
                    <CardVisual card={card} className="w-full shadow-xl mb-8 transform hover:scale-105 transition-transform duration-300" />

                    <div className="w-full space-y-4">
                        <div className="flex justify-between items-center p-3 bg-white rounded-lg border border-slate-200 shadow-sm">
                            <span className="text-sm font-medium text-slate-500">Status</span>
                            <span className={`px-2 py-1 rounded-md text-xs font-bold ${card.cardStatus === 'ACTIVE' ? 'bg-green-100 text-green-700' : 'bg-slate-100 text-slate-700'}`}>
                                {card.cardStatus}
                            </span>
                        </div>
                        <div className="flex justify-between items-center p-3 bg-white rounded-lg border border-slate-200 shadow-sm">
                            <span className="text-sm font-medium text-slate-500">Last Updated</span>
                            <span className="text-sm font-mono text-slate-700">
                                {new Date(card.lastUpdateTime).toLocaleDateString()}
                            </span>
                        </div>
                    </div>
                </div>

                {/* Right Side - Update Form */}
                <div className="w-full md:w-7/12 p-8">
                    <div className="mb-6">
                        <h2 className="text-2xl font-bold text-slate-800">Card Details</h2>
                        <p className="text-slate-500">View and update limits for this card.</p>
                    </div>

                    {error && (
                        <div className="mb-4 p-3 bg-red-50 text-red-700 text-sm rounded-lg flex items-center gap-2">
                            <AlertCircle className="w-4 h-4" />
                            {error}
                        </div>
                    )}

                    <form onSubmit={handleSubmit} className="space-y-5">
                        <div className="grid grid-cols-2 gap-5">
                            <div className="space-y-2">
                                <label className="text-sm font-medium text-slate-700">Expire Date (MM/YY)</label>
                                <input
                                    type="text"
                                    name="expireDate"
                                    value={formData.expireDate}
                                    onChange={handleChange}
                                    className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all"
                                    placeholder="MM/YY"
                                />
                            </div>
                            <div className="space-y-2">
                                <label className="text-sm font-medium text-slate-700">Credit Limit</label>
                                <input
                                    type="number"
                                    name="creditLimit"
                                    value={formData.creditLimit}
                                    onChange={handleChange}
                                    className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all"
                                />
                            </div>
                            <div className="space-y-2">
                                <label className="text-sm font-medium text-slate-700">Cash Limit</label>
                                <input
                                    type="number"
                                    name="cashLimit"
                                    value={formData.cashLimit}
                                    onChange={handleChange}
                                    className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all"
                                />
                            </div>
                            <div className="space-y-2">
                                <label className="text-sm font-medium text-slate-700">Avail. Credit</label>
                                <input
                                    type="number"
                                    name="availableCreditLimit"
                                    value={formData.availableCreditLimit}
                                    onChange={handleChange}
                                    className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all"
                                />
                            </div>
                            <div className="space-y-2">
                                <label className="text-sm font-medium text-slate-700">Avail. Cash</label>
                                <input
                                    type="number"
                                    name="availableCashLimit"
                                    value={formData.availableCashLimit}
                                    onChange={handleChange}
                                    className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all"
                                />
                            </div>
                        </div>

                        <div className="pt-4 flex justify-end gap-3">
                            <button
                                type="button"
                                onClick={onClose}
                                className="px-4 py-2 text-slate-600 hover:bg-slate-100 rounded-lg transition-colors"
                            >
                                Cancel
                            </button>
                            <button
                                type="submit"
                                disabled={loading}
                                className="px-6 py-2 bg-slate-900 text-white rounded-lg hover:bg-slate-800 transition-colors flex items-center gap-2 disabled:opacity-50 disabled:cursor-not-allowed shadow-md hover:shadow-lg"
                            >
                                {loading ? (
                                    <>Saving...</>
                                ) : (
                                    <>
                                        <Save className="w-4 h-4" />
                                        Update Details
                                    </>
                                )}
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default CardDetailsModal;
