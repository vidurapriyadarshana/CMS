import { Calendar, Tag, Activity, FileText, CheckCircle2, XCircle, Clock, Ban } from 'lucide-react';
import type { CardRequestData } from '../types/card';
import { cn } from '../lib/utils';

interface ConfirmRequestTableProps {
    requests: CardRequestData[];
    onChangeStatus: (request: CardRequestData) => void;
}

const ConfirmRequestTable = ({ requests, onChangeStatus }: ConfirmRequestTableProps) => {
    return (
        <div className="overflow-x-auto rounded-xl border border-slate-200 shadow-sm bg-white">
            <table className="w-full text-sm text-left">
                <thead className="bg-slate-50 border-b border-slate-200 text-slate-500 font-semibold uppercase text-xs tracking-wider">
                    <tr>
                        <th className="px-6 py-4">Card Number</th>
                        <th className="px-6 py-4">Reason Code</th>
                        <th className="px-6 py-4">Remark</th>
                        <th className="px-6 py-4">Current Status</th>
                        <th className="px-6 py-4">Created Time</th>
                        <th className="px-6 py-4">Request Status</th>
                        <th className="px-6 py-4 text-center">Actions</th>
                    </tr>
                </thead>
                <tbody className="divide-y divide-slate-100 bg-white">
                    {requests.map((request) => (
                        <tr
                            key={request.requestId}
                            className="hover:bg-blue-50/50 transition-colors group cursor-pointer"
                            onClick={() => onChangeStatus(request)}
                        >
                            <td className="px-6 py-4 font-mono font-medium text-slate-900 group-hover:text-blue-600 transition-colors">
                                {request.cardNumber}
                            </td>
                            <td className="px-6 py-4 text-slate-600">
                                <div className="flex items-center gap-2">
                                    <Tag className="w-4 h-4 text-slate-400" />
                                    {request.requestReasonCode}
                                </div>
                            </td>
                            <td className="px-6 py-4 text-slate-600 truncate max-w-50" title={request.remark}>
                                <div className="flex items-center gap-2">
                                    <FileText className="w-4 h-4 text-slate-400" />
                                    {request.remark || '-'}
                                </div>
                            </td>
                            <td className="px-6 py-4">
                                <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-slate-100 text-slate-800`}>
                                    {request.status}
                                </span>
                            </td>
                            <td className="px-6 py-4 text-slate-600 whitespace-nowrap">
                                <div className="flex items-center gap-2">
                                    <Calendar className="w-4 h-4 text-slate-400" />
                                    {new Date(request.createdTime).toLocaleString()}
                                </div>
                            </td>
                            <td className="px-6 py-4">
                                <span className={cn(
                                    "inline-flex items-center gap-1.5 px-2.5 py-1 rounded-full text-[10px] font-bold tracking-widest uppercase border border-opacity-50",
                                    request.completionStatus === 'COMPLETED' ? 'bg-green-50 text-green-700 border-green-200' :
                                        request.completionStatus === 'FAILED' ? 'bg-red-50 text-red-700 border-red-200' :
                                            request.completionStatus === 'DEACTIVATED' ? 'bg-slate-100 text-slate-700 border-slate-200' :
                                                'bg-amber-50 text-amber-700 border-amber-200'
                                )}>
                                    {request.completionStatus === 'COMPLETED' ? <CheckCircle2 className="w-3.5 h-3.5" /> :
                                        request.completionStatus === 'FAILED' ? <XCircle className="w-3.5 h-3.5" /> :
                                            request.completionStatus === 'DEACTIVATED' ? <Ban className="w-3.5 h-3.5" /> :
                                                <Clock className="w-3.5 h-3.5" />}
                                    {request.completionStatus || 'PENDING'}
                                </span>
                            </td>
                            <td className="px-6 py-4 text-center">
                                <button
                                    onClick={(e) => {
                                        e.stopPropagation();
                                        onChangeStatus(request);
                                    }}
                                    className="p-2 text-slate-400 hover:text-blue-600 hover:bg-blue-100 rounded-lg transition-colors"
                                    title="Change Status"
                                >
                                    <Activity className="w-4 h-4" />
                                </button>
                            </td>
                        </tr>
                    ))}
                    {requests.length === 0 && (
                        <tr>
                            <td colSpan={7} className="px-6 py-12 text-center text-slate-400">
                                No card requests found.
                            </td>
                        </tr>
                    )}
                </tbody>
            </table>
        </div>
    );
};

export default ConfirmRequestTable;
