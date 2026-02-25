import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { FileText, FileSpreadsheet, Loader2 } from 'lucide-react';
import axios from 'axios';
import { toast } from 'sonner';
import { fetchStatusTypes, fetchRequestTypes } from '../store/slices/optionsSlice';
import type { RootState, AppDispatch } from '../store/index';

const Reports = () => {
    const dispatch = useDispatch<AppDispatch>();
    const { statusTypes, requestTypes } = useSelector((state: RootState) => state.options);

    // Card Filter
    const [cardFilterStatus, setCardFilterStatus] = useState<string>('');

    // Request Filters
    const [requestFilterStatus, setRequestFilterStatus] = useState<string>('');
    const [requestFilterReason, setRequestFilterReason] = useState<string>('');
    const [isDownloading, setIsDownloading] = useState(false);

    useEffect(() => {
        dispatch(fetchStatusTypes());
        dispatch(fetchRequestTypes());
    }, [dispatch]);

    const getCardUrl = (base: string) => {
        return cardFilterStatus ? `${base}?cardStatus=${cardFilterStatus}` : base;
    };

    const getRequestUrl = (base: string) => {
        const params = new URLSearchParams();
        if (requestFilterStatus) params.append('requestStatus', requestFilterStatus);
        if (requestFilterReason) params.append('requestReasonCode', requestFilterReason);

        const queryString = params.toString();
        return queryString ? `${base}?${queryString}` : base;
    };

    const handleDownload = async (url: string, defaultFilename: string) => {
        setIsDownloading(true);
        try {
            const response = await axios.get(url, { responseType: 'blob' });

            // Try to get filename from content-disposition header
            let filename = defaultFilename;
            const disposition = response.headers['content-disposition'];
            if (disposition && disposition.indexOf('filename=') !== -1) {
                const filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
                const matches = filenameRegex.exec(disposition);
                if (matches != null && matches[1]) {
                    filename = matches[1].replace(/['"]/g, '');
                }
            }

            // Create a blob URL and trigger download
            const blobUrl = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = blobUrl;
            link.setAttribute('download', filename);
            document.body.appendChild(link);
            link.click();
            link.parentNode?.removeChild(link);
            window.URL.revokeObjectURL(blobUrl);

            toast.success(`${defaultFilename} downloaded successfully`);
        } catch (error: any) {
            if (error.response && error.response.data && error.response.data instanceof Blob) {
                try {
                    const text = await error.response.data.text();
                    const errorData = JSON.parse(text);
                    const message = errorData.data || errorData.message || errorData.status || 'Failed to download report';
                    toast.error(message);
                } catch {
                    toast.error('Failed to download report');
                }
            } else {
                toast.error(error.message || 'Failed to download report');
            }
        } finally {
            setIsDownloading(false);
        }
    };
    return (
        <div className="p-8 max-w-4xl mx-auto">
            <div className="mb-10">
                <h1 className="text-3xl font-extrabold text-slate-900 tracking-tight">Reports & Exports</h1>
                <p className="text-slate-500 mt-2 text-base">Generate and download PDF and CSV reports for cards and requests.</p>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                {/* Cards Reports */}
                <div className="bg-white p-8 rounded-2xl border border-slate-200 shadow-sm transition-shadow hover:shadow-md">
                    <h2 className="text-xl font-bold text-slate-800 mb-2">Card Reports</h2>
                    <p className="text-slate-500 text-sm mb-6">Export a comprehensive list of all managed cards.</p>

                    <div className="mb-6 flex gap-3">
                        <div className="flex-1 bg-slate-50 border border-slate-200 rounded-xl px-3 py-2">
                            <label className="block text-xs font-semibold text-slate-500 mb-1">Filter by Status</label>
                            <select
                                value={cardFilterStatus}
                                onChange={(e) => setCardFilterStatus(e.target.value)}
                                className="w-full bg-transparent text-sm font-medium text-slate-900 focus:outline-none cursor-pointer"
                            >
                                <option value="">All Statuses</option>
                                {statusTypes.map((status) => (
                                    <option key={status.statusCode} value={status.statusCode}>
                                        {status.description}
                                    </option>
                                ))}
                            </select>
                        </div>
                    </div>

                    <div className="flex gap-4">
                        <button
                            onClick={() => handleDownload(getCardUrl('/reports/cards/pdf'), 'cards_report.pdf')}
                            disabled={isDownloading}
                            className="flex-1 flex items-center justify-center gap-2 px-4 py-3 bg-rose-50 text-rose-700 hover:bg-rose-100 rounded-xl font-bold transition-colors shadow-sm focus:outline-none focus:ring-2 focus:ring-rose-500 focus:ring-offset-2 active:scale-95 disabled:opacity-50 disabled:cursor-not-allowed"
                            title="Export Cards to PDF"
                        >
                            {isDownloading ? <Loader2 className="w-5 h-5 animate-spin" /> : <FileText className="w-5 h-5" />}
                            Download PDF
                        </button>
                        <button
                            onClick={() => handleDownload(getCardUrl('/reports/cards/csv'), 'cards_report.csv')}
                            disabled={isDownloading}
                            className="flex-1 flex items-center justify-center gap-2 px-4 py-3 bg-emerald-50 text-emerald-700 hover:bg-emerald-100 rounded-xl font-bold transition-colors shadow-sm focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:ring-offset-2 active:scale-95 disabled:opacity-50 disabled:cursor-not-allowed"
                            title="Export Cards to CSV"
                        >
                            {isDownloading ? <Loader2 className="w-5 h-5 animate-spin" /> : <FileSpreadsheet className="w-5 h-5" />}
                            Download CSV
                        </button>
                    </div>
                </div>

                {/* Card Requests Reports */}
                <div className="bg-white p-8 rounded-2xl border border-slate-200 shadow-sm transition-shadow hover:shadow-md">
                    <h2 className="text-xl font-bold text-slate-800 mb-2">Card Request Reports</h2>
                    <p className="text-slate-500 text-sm mb-6">Export a list of all incoming card requests and their statuses.</p>

                    <div className="mb-6 flex gap-3">
                        <div className="flex-1 bg-slate-50 border border-slate-200 rounded-xl px-3 py-2">
                            <label className="block text-xs font-semibold text-slate-500 mb-1">Filter by Reason</label>
                            <select
                                value={requestFilterReason}
                                onChange={(e) => setRequestFilterReason(e.target.value)}
                                className="w-full bg-transparent text-sm font-medium text-slate-900 focus:outline-none cursor-pointer"
                            >
                                <option value="">All Reasons</option>
                                {requestTypes.map((type) => (
                                    <option key={type.code} value={type.code}>
                                        {type.description}
                                    </option>
                                ))}
                            </select>
                        </div>
                        <div className="flex-1 bg-slate-50 border border-slate-200 rounded-xl px-3 py-2">
                            <label className="block text-xs font-semibold text-slate-500 mb-1">Filter by Status</label>
                            <select
                                value={requestFilterStatus}
                                onChange={(e) => setRequestFilterStatus(e.target.value)}
                                className="w-full bg-transparent text-sm font-medium text-slate-900 focus:outline-none cursor-pointer"
                            >
                                <option value="">All Statuses</option>
                                <option value="PENDING">Pending</option>
                                <option value="COMPLETE">Completed</option>
                                <option value="FAILED">Failed</option>
                                <option value="DEACTIVATED">Deactivated</option>
                            </select>
                        </div>
                    </div>

                    <div className="flex gap-4">
                        <button
                            onClick={() => handleDownload(getRequestUrl('/reports/card-requests/pdf'), 'card_requests_report.pdf')}
                            disabled={isDownloading}
                            className="flex-1 flex items-center justify-center gap-2 px-4 py-3 bg-rose-50 text-rose-700 hover:bg-rose-100 rounded-xl font-bold transition-colors shadow-sm focus:outline-none focus:ring-2 focus:ring-rose-500 focus:ring-offset-2 active:scale-95 disabled:opacity-50 disabled:cursor-not-allowed"
                            title="Export Card Requests to PDF"
                        >
                            {isDownloading ? <Loader2 className="w-5 h-5 animate-spin" /> : <FileText className="w-5 h-5" />}
                            Download PDF
                        </button>
                        <button
                            onClick={() => handleDownload(getRequestUrl('/reports/card-requests/csv'), 'card_requests_report.csv')}
                            disabled={isDownloading}
                            className="flex-1 flex items-center justify-center gap-2 px-4 py-3 bg-emerald-50 text-emerald-700 hover:bg-emerald-100 rounded-xl font-bold transition-colors shadow-sm focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:ring-offset-2 active:scale-95 disabled:opacity-50 disabled:cursor-not-allowed"
                            title="Export Card Requests to CSV"
                        >
                            {isDownloading ? <Loader2 className="w-5 h-5 animate-spin" /> : <FileSpreadsheet className="w-5 h-5" />}
                            Download CSV
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Reports;
