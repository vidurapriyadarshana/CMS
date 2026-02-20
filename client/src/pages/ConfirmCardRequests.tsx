import { useEffect, useState, useCallback } from 'react';
import axios from 'axios';
import { Loader2 } from 'lucide-react';
import { toast } from 'sonner';
import type { CardRequestData, CommonResponse } from '../types/card';
import ConfirmRequestTable from '../components/ConfirmRequestTable';
import ChangeStatusModal from '../components/ChangeStatusModal';

const ConfirmCardRequests = () => {
    const [requests, setRequests] = useState<CardRequestData[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const [selectedRequest, setSelectedRequest] = useState<CardRequestData | null>(null);
    const [isModalOpen, setIsModalOpen] = useState(false);

    const fetchRequests = useCallback(async () => {
        // Only set hard loading if we have no data to prevent table flickering
        if (requests.length === 0) {
            setLoading(true);
        }
        setError(null);

        try {
            const response = await axios.get<CommonResponse<CardRequestData[]>>('/card-requests');
            if (response.data.code === 200 && Array.isArray(response.data.data)) {
                // Sort by requestId descending or createdTime descending
                const sorted = response.data.data.sort((a, b) => b.requestId - a.requestId);
                setRequests(sorted);
            } else {
                setError(response.data.status || 'Failed to fetch card requests');
            }
        } catch (err: any) {
            console.error('Error fetching card requests:', err);
            const errorMessage = err.response?.data?.status || err.message || 'Error fetching card requests';

            // If we already have data, just toast the error instead of taking down the UI
            if (requests.length > 0) {
                toast.error(errorMessage);
            } else {
                setError(errorMessage);
            }
        } finally {
            setLoading(false);
        }
    }, [requests.length]);

    useEffect(() => {
        fetchRequests();
    }, [fetchRequests]);

    const handleOpenStatusModal = (request: CardRequestData) => {
        setSelectedRequest(request);
        setIsModalOpen(true);
    };

    return (
        <div className="p-8 max-w-[1600px] mx-auto">
            <div className="mb-8 flex justify-between items-end">
                <div>
                    <h1 className="text-3xl font-bold text-slate-900 tracking-tight">Confirm Requests</h1>
                    <p className="text-slate-500 mt-2">Manage and update the status of incoming card requests.</p>
                </div>
                <div className="text-right">
                    <span className="text-sm font-medium text-slate-500 block">Total Requests</span>
                    <p className="text-2xl font-bold text-slate-900 leading-none">{requests.length}</p>
                </div>
            </div>

            {loading && requests.length === 0 ? (
                <div className="flex h-96 items-center justify-center">
                    <Loader2 className="w-8 h-8 animate-spin text-slate-400" />
                </div>
            ) : error && requests.length === 0 ? (
                <div className="bg-red-50 border border-red-200 rounded-xl p-6 text-center text-red-600">
                    <p>{error}</p>
                    <button
                        onClick={fetchRequests}
                        className="mt-4 px-4 py-2 bg-white border border-red-200 rounded-lg text-sm font-medium hover:bg-red-50 transition-colors"
                    >
                        Try Again
                    </button>
                </div>
            ) : (
                <ConfirmRequestTable
                    requests={requests}
                    onChangeStatus={handleOpenStatusModal}
                />
            )}

            <ChangeStatusModal
                isOpen={isModalOpen}
                onClose={() => {
                    setIsModalOpen(false);
                    setSelectedRequest(null);
                }}
                request={selectedRequest}
                onStatusUpdated={fetchRequests}
            />
        </div>
    );
};

export default ConfirmCardRequests;
