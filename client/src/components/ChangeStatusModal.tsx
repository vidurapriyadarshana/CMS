import React, { useState, useEffect } from 'react';
import { Loader2, X } from 'lucide-react';
import axios from 'axios';
import { toast } from 'sonner';
import type { CardRequestData, StatusType, CommonResponse } from '../types/card';

interface ChangeStatusModalProps {
    isOpen: boolean;
    onClose: () => void;
    request: CardRequestData | null;
    onStatusUpdated: () => void;
}

const ChangeStatusModal: React.FC<ChangeStatusModalProps> = ({ isOpen, onClose, request, onStatusUpdated }) => {
    const [isLoading, setIsLoading] = useState(false);
    const [statusTypes, setStatusTypes] = useState<StatusType[]>([]);
    const [isFetchingStatuses, setIsFetchingStatuses] = useState(false);

    const [selectedStatus, setSelectedStatus] = useState('');
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (isOpen) {
            fetchStatusTypes();
            setSelectedStatus('');
            setError(null);
        }
    }, [isOpen]);

    const fetchStatusTypes = async () => {
        setIsFetchingStatuses(true);
        try {
            const response = await axios.get<CommonResponse<StatusType[]>>('/status');
            if (response.data.code === 200 && Array.isArray(response.data.data)) {
                setStatusTypes(response.data.data);
                if (response.data.data.length > 0) {
                    setSelectedStatus(response.data.data[0].statusCode);
                }
            } else {
                toast.error(response.data.status || 'Failed to load statuses');
            }
        } catch (err: any) {
            console.error('Error fetching statuses', err);
            toast.error(err.response?.data?.status || err.message || 'Failed to load statuses');
        } finally {
            setIsFetchingStatuses(false);
        }
    };

    if (!isOpen || !request) return null;

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (!selectedStatus) {
            setError('Please select a new status');
            return;
        }

        setIsLoading(true);
        setError(null);
        try {
            const payload = {
                status: selectedStatus
            };

            const response = await axios.put<CommonResponse<string>>(`/card-requests/${request.encryptedCardNumber}/status`, payload);

            if (response.data.code === 200) {
                toast.success(response.data.message || 'Request status updated successfully');
                onStatusUpdated();
                onClose();
            } else {
                const errMsg = response.data.message || response.data.status || 'Failed to update request status';
                setError(errMsg);
                toast.error(errMsg);
            }
        } catch (err: any) {
            let serverMessage = 'Failed to update request status';
            if (err.response?.data) {
                // Determine if data contains the error message string (as we've seen in some backend error responses)
                if (typeof err.response.data.data === 'string') {
                    serverMessage = err.response.data.data;
                } else if (typeof err.response.data.data?.message === 'string') {
                    serverMessage = err.response.data.data.message;
                } else {
                    serverMessage = err.response.data.message || err.response.data.status || serverMessage;
                }
            } else if (err.message) {
                serverMessage = err.message;
            }
            setError(serverMessage);
            toast.error(serverMessage);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/50 backdrop-blur-sm animate-in fade-in duration-200">
            <div className="bg-white rounded-2xl shadow-xl w-full max-w-md overflow-hidden animate-in zoom-in-95 duration-200">
                <div className="px-6 py-4 border-b border-slate-100 flex justify-between items-center bg-slate-50/50">
                    <h3 className="text-lg font-semibold text-slate-900">Change Request Status</h3>
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

                    <div className="bg-slate-50 p-3 rounded-lg border border-slate-100 mb-4 text-sm space-y-1">
                        <div className="flex justify-between">
                            <span className="text-slate-500">Request ID</span>
                            <span className="font-medium text-slate-900">#{request.requestId}</span>
                        </div>
                        <div className="flex justify-between">
                            <span className="text-slate-500">Card</span>
                            <span className="font-mono text-slate-900">{request.cardNumber}</span>
                        </div>
                        <div className="flex justify-between">
                            <span className="text-slate-500">Current Status</span>
                            <span className="font-medium text-slate-900">{request.status}</span>
                        </div>
                    </div>

                    <div className="space-y-4">
                        <div>
                            <label className="block text-sm font-medium text-slate-700 mb-1">New Status</label>
                            {isFetchingStatuses ? (
                                <div className="flex items-center gap-2 px-3 py-2 border border-slate-300 rounded-lg bg-slate-50 text-slate-500 text-sm">
                                    <Loader2 className="w-4 h-4 animate-spin" />
                                    Loading options...
                                </div>
                            ) : (
                                <select
                                    required
                                    value={selectedStatus}
                                    onChange={(e) => setSelectedStatus(e.target.value)}
                                    className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all bg-white"
                                >
                                    <option value="" disabled>Select a status...</option>
                                    {statusTypes.map((status) => (
                                        <option key={status.statusCode} value={status.statusCode}>
                                            {status.description} ({status.statusCode})
                                        </option>
                                    ))}
                                </select>
                            )}
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
                            disabled={isLoading || isFetchingStatuses}
                            className="flex-1 px-4 py-2 bg-blue-600 text-white rounded-lg font-medium hover:bg-blue-700 transition-colors flex items-center justify-center disabled:opacity-50"
                        >
                            {isLoading ? <Loader2 className="w-5 h-5 animate-spin" /> : 'Update Status'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default ChangeStatusModal;
