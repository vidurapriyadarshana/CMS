import React, { useState, useEffect } from 'react';
import { Loader2, X } from 'lucide-react';
import axios from 'axios';
import { toast } from 'sonner';
import { useDispatch, useSelector } from 'react-redux';
import { fetchRequestTypes } from '../store/slices/optionsSlice';
import type { RootState, AppDispatch } from '../store/index';
import type { CardResponse, SendCardRequestPayload, CommonResponse } from '../types/card';
import CardVisual from './CardVisual';

interface User {
    userName: string;
    status: string;
    name: string;
}

interface SendRequestModalProps {
    isOpen: boolean;
    onClose: () => void;
    card: CardResponse | null;
}

const SendRequestModal: React.FC<SendRequestModalProps> = ({ isOpen, onClose, card }) => {
    const dispatch = useDispatch<AppDispatch>();
    const { requestTypes, loadingRequestTypes } = useSelector((state: RootState) => state.options);

    const [isLoading, setIsLoading] = useState(false);
    const [users, setUsers] = useState<User[]>([]);
    const [formData, setFormData] = useState({
        requestReasonCode: '',
        remark: '',
        requestedUser: ''
    });
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (isOpen) {
            if (requestTypes.length === 0) {
                dispatch(fetchRequestTypes());
            }
            axios.get('/users').then(res => {
                if (res.data && res.data.code === 200) {
                    setUsers(res.data.data);
                }
            }).catch(err => {
                console.error('Failed to fetch users', err);
            });
            // Reset form when modal opens
            setFormData({
                requestReasonCode: '',
                remark: '',
                requestedUser: ''
            });
            setError(null);
        }
    }, [isOpen, dispatch, requestTypes.length]);

    useEffect(() => {
        if (isOpen && requestTypes.length > 0 && !formData.requestReasonCode) {
            setFormData(prev => ({ ...prev, requestReasonCode: requestTypes[0].code }));
        }
    }, [isOpen, requestTypes, formData.requestReasonCode]);

    if (!isOpen || !card) return null;

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (!formData.requestReasonCode) {
            setError('Please select a request reason');
            return;
        }

        setIsLoading(true);
        setError(null);
        try {
            const payload: SendCardRequestPayload = {
                requestReasonCode: formData.requestReasonCode,
                remark: formData.remark,
                cardNumber: card.encryptedCardNumber, // Using the backend 'id' representing the card
                status: card.cardStatus, // Send current card status as requested
                requestedUser: formData.requestedUser
            };

            const response = await axios.post<CommonResponse<string>>('/card-requests', payload);

            if (response.data.code === 201 || response.data.code === 200) {
                toast.success(response.data.message || 'Card request submitted successfully');
                onClose();
            } else {
                setError(response.data.status || 'Failed to submit card request');
            }
        } catch (err: any) {
            if (err.response && err.response.data) {
                const serverMessage = err.response.data.data || err.response.data.message || err.response.data.status;
                setError(serverMessage || 'Failed to submit card request');
            } else {
                setError(err.message || 'Failed to submit card request');
            }
        } finally {
            setIsLoading(false);
        }
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/50 backdrop-blur-sm animate-in fade-in duration-200">
            <div className="bg-white rounded-2xl shadow-xl w-full max-w-md overflow-hidden animate-in zoom-in-95 duration-200">
                <div className="px-6 py-4 border-b border-slate-100 flex justify-between items-center bg-slate-50/50">
                    <h3 className="text-lg font-semibold text-slate-900">Send Card Request</h3>
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

                    <div className="flex justify-center mb-6 overflow-hidden">
                        <div className="scale-[0.80] origin-top">
                            <CardVisual card={card} />
                        </div>
                    </div>

                    <div className="space-y-4 -mt-8">
                        <div>
                            <label className="block text-sm font-medium text-slate-700 mb-1">Request Reason</label>
                            {loadingRequestTypes ? (
                                <div className="flex items-center gap-2 px-3 py-2 border border-slate-300 rounded-lg bg-slate-50 text-slate-500 text-sm">
                                    <Loader2 className="w-4 h-4 animate-spin" />
                                    Loading options...
                                </div>
                            ) : (
                                <select
                                    name="requestReasonCode"
                                    required
                                    value={formData.requestReasonCode}
                                    onChange={handleChange}
                                    className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all bg-white"
                                >
                                    <option value="" disabled>Select a reason...</option>
                                    {requestTypes.map((type) => (
                                        <option key={type.code} value={type.code}>
                                            {type.description}
                                        </option>
                                    ))}
                                </select>
                            )}
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-slate-700 mb-1">Remark</label>
                            <input
                                type="text"
                                name="remark"
                                required
                                value={formData.remark}
                                onChange={handleChange}
                                placeholder="Enter a remark or reason details"
                                className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all"
                            />
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-slate-700 mb-1">Requested User</label>
                            <select
                                name="requestedUser"
                                required
                                value={formData.requestedUser}
                                onChange={handleChange}
                                className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all appearance-none bg-white"
                            >
                                <option value="" disabled>Select User</option>
                                {users.map(user => (
                                    <option key={user.userName} value={user.userName}>
                                        {user.name}
                                    </option>
                                ))}
                            </select>
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
                            disabled={isLoading || loadingRequestTypes}
                            className="flex-1 px-4 py-2 bg-blue-600 text-white rounded-lg font-medium hover:bg-blue-700 transition-colors flex items-center justify-center disabled:opacity-50"
                        >
                            {isLoading ? <Loader2 className="w-5 h-5 animate-spin" /> : 'Send Request'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default SendRequestModal;
