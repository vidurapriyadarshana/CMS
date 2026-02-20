
import React, { useState } from 'react';
import { Loader2, X } from 'lucide-react';
import { cn } from '../lib/utils';
import type { CardRequest } from '../types/card';

interface CreateCardModalProps {
    isOpen: boolean;
    onClose: () => void;
    onCreate: (data: CardRequest) => Promise<void>;
}

const CreateCardModal: React.FC<CreateCardModalProps> = ({ isOpen, onClose, onCreate }) => {
    const [isLoading, setIsLoading] = useState(false);
    const [formData, setFormData] = useState<CardRequest>({
        cardNumber: '',
        expireDate: '',
        creditLimit: 50000,
        cashLimit: 20000,
        availableCreditLimit: 50000,
        availableCashLimit: 20000,
    });
    const [error, setError] = useState<string | null>(null);

    if (!isOpen) return null;

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setIsLoading(true);
        setError(null);
        try {
            await onCreate(formData);
            onClose();
        } catch (err: any) {
            setError(err.message || 'Failed to create card');
        } finally {
            setIsLoading(false);
        }
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: name.includes('Limit') ? Number(value) : value
        }));
    };

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/50 backdrop-blur-sm animate-in fade-in duration-200">
            <div className="bg-white rounded-2xl shadow-xl w-full max-w-lg overflow-hidden animate-in zoom-in-95 duration-200">
                <div className="px-6 py-4 border-b border-slate-100 flex justify-between items-center bg-slate-50/50">
                    <h3 className="text-lg font-semibold text-slate-900">Create New Card</h3>
                    <button onClick={onClose} className="text-slate-400 hover:text-slate-600 transition-colors">
                        <X className="w-5 h-5" />
                    </button>
                </div>

                <form onSubmit={handleSubmit} className="p-6 space-y-4">
                    {error && (
                        <div className="bg-red-50 text-red-600 px-4 py-3 rounded-lg text-sm">
                            {error}
                        </div>
                    )}

                    <div className="space-y-4">
                        <div>
                            <label className="block text-sm font-medium text-slate-700 mb-1">Card Number</label>
                            <input
                                type="text"
                                name="cardNumber"
                                required
                                pattern="[0-9]{16}"
                                maxLength={16}
                                value={formData.cardNumber}
                                onChange={handleChange}
                                placeholder="1234567890123456"
                                className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all"
                            />
                            <p className="text-xs text-slate-500 mt-1">16 digits required</p>
                        </div>

                        <div className="grid grid-cols-1 gap-4">
                            <div>
                                <label className="block text-sm font-medium text-slate-700 mb-1">Expiry Date</label>
                                <input
                                    type="text"
                                    name="expireDate"
                                    required
                                    placeholder="MM/YY"
                                    pattern="(0[1-9]|1[0-2])\/[0-9]{2}"
                                    value={formData.expireDate}
                                    onChange={handleChange}
                                    className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all"
                                />
                            </div>
                        </div>

                        <div className="grid grid-cols-2 gap-4">
                            <div>
                                <label className="block text-sm font-medium text-slate-700 mb-1">Credit Limit</label>
                                <input
                                    type="number"
                                    name="creditLimit"
                                    required
                                    min="0"
                                    value={formData.creditLimit}
                                    onChange={handleChange}
                                    className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all"
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-slate-700 mb-1">Cash Limit</label>
                                <input
                                    type="number"
                                    name="cashLimit"
                                    required
                                    min="0"
                                    value={formData.cashLimit}
                                    onChange={handleChange}
                                    className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all"
                                />
                            </div>
                        </div>
                        {/* Assuming Available limits default to full limits on creation for simplicity, or user can edit them? 
                            The API might infer them or require them. Card.java has them. I'll add fields but default them to limits.
                        */}
                        <div className="grid grid-cols-2 gap-4">
                            <div>
                                <label className="block text-sm font-medium text-slate-700 mb-1">Avail. Credit</label>
                                <input
                                    type="number"
                                    name="availableCreditLimit"
                                    required
                                    min="0"
                                    value={formData.availableCreditLimit}
                                    onChange={handleChange}
                                    className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all"
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-slate-700 mb-1">Avail. Cash</label>
                                <input
                                    type="number"
                                    name="availableCashLimit"
                                    required
                                    min="0"
                                    value={formData.availableCashLimit}
                                    onChange={handleChange}
                                    className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all"
                                />
                            </div>
                        </div>

                    </div>

                    <div className="flex gap-3 pt-4 border-t border-slate-100 mt-6">
                        <button
                            type="button"
                            onClick={onClose}
                            className="flex-1 px-4 py-2 border border-slate-200 rounded-lg text-slate-700 font-medium hover:bg-slate-50 transition-colors"
                        >
                            Cancel
                        </button>
                        <button
                            type="submit"
                            disabled={isLoading}
                            className="flex-1 px-4 py-2 bg-blue-600 text-white rounded-lg font-medium hover:bg-blue-700 transition-colors flex items-center justify-center disabled:opacity-50"
                        >
                            {isLoading ? <Loader2 className="w-5 h-5 animate-spin" /> : 'Create Card'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default CreateCardModal;
