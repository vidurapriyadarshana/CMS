import { useEffect, useState } from 'react';
import { Loader2 } from 'lucide-react';
import { useDispatch, useSelector } from 'react-redux';
import { fetchCardRequests } from '../store/slices/cardRequestSlice';
import type { RootState, AppDispatch } from '../store/index';
import type { CardRequestData } from '../types/card';
import ConfirmRequestTable from '../components/ConfirmRequestTable';
import ChangeStatusModal from '../components/ChangeStatusModal';

const ConfirmCardRequests = () => {
    const dispatch = useDispatch<AppDispatch>();
    const { requests, loading, error } = useSelector((state: RootState) => state.cardRequests);

    const [selectedRequest, setSelectedRequest] = useState<CardRequestData | null>(null);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [filterStatus, setFilterStatus] = useState<string>('ALL');

    const filteredRequests = requests.filter(request => {
        if (filterStatus === 'ALL') return true;
        // Default to PENDING if completionStatus is null/undefined
        const status = request.completionStatus || 'PENDING';
        return status === filterStatus;
    });

    useEffect(() => {
        dispatch(fetchCardRequests());
    }, [dispatch]);

    const handleOpenStatusModal = (request: CardRequestData) => {
        setSelectedRequest(request);
        setIsModalOpen(true);
    };

    return (
        <div className="p-8 max-w-[1600px] mx-auto">
            <div className="mb-8 flex justify-between items-end">
                <div>
                    <h1 className="text-3xl font-extrabold text-slate-900 tracking-tight">Confirm Requests</h1>
                    <p className="text-slate-500 mt-2 text-base">Manage and update the status of incoming card requests.</p>
                </div>
                <div className="text-right flex items-center gap-6">
                    <div className="flex items-center gap-3 bg-white px-4 py-2.5 rounded-xl border border-slate-200 shadow-sm">
                        <label className="text-sm font-semibold text-slate-600">Filter By:</label>
                        <select
                            value={filterStatus}
                            onChange={(e) => setFilterStatus(e.target.value)}
                            className="bg-transparent text-sm font-medium text-slate-900 focus:outline-none cursor-pointer"
                        >
                            <option value="ALL">All Requests</option>
                            <option value="PENDING">Pending</option>
                            <option value="COMPLETED">Completed</option>
                            <option value="FAILED">Failed</option>
                            <option value="DEACTIVATED">Deactivated</option>
                        </select>
                    </div>
                    <div className="bg-white px-5 py-2.5 rounded-xl border border-slate-200 shadow-sm flex flex-col items-end">
                        <span className="text-[11px] font-bold text-slate-400 uppercase tracking-widest leading-none mb-1">Total Requests</span>
                        <p className="text-2xl font-black text-slate-800 leading-none">{requests.length}</p>
                    </div>
                </div>
            </div>

            {loading && requests.length === 0 ? (
                <div className="flex h-96 items-center justify-center">
                    <Loader2 className="w-8 h-8 animate-spin text-blue-500" />
                </div>
            ) : error && requests.length === 0 ? (
                <div className="bg-red-50 border border-red-200 rounded-xl p-8 text-center text-red-600 shadow-sm">
                    <p className="text-lg font-medium">{error}</p>
                    <button
                        onClick={() => dispatch(fetchCardRequests())}
                        className="mt-6 px-6 py-2.5 bg-white border border-red-200 rounded-xl text-sm font-semibold hover:bg-red-50 transition-colors shadow-sm focus:outline-none focus:ring-2 focus:ring-red-500 focus:ring-offset-2"
                    >
                        Try Again
                    </button>
                </div>
            ) : (
                <ConfirmRequestTable
                    requests={filteredRequests}
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
                onStatusUpdated={() => dispatch(fetchCardRequests())}
            />
        </div>
    );
};

export default ConfirmCardRequests;
