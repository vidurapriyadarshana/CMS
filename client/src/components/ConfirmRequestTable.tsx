import { Calendar, Hash, Tag, Activity, FileText, CheckCircle2, XCircle, Clock } from 'lucide-react';
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
                <thead className="bg-slate-50 text-slate-500 font-medium uppercase text-xs border-b border-slate-200">
                    <tr>
                        <th className="px-6 py-4">Request ID</th>
                        <th className="px-6 py-4">Card Number</th>
                        <th className="px-6 py-4">Reason Code</th>
                        <th className="px-6 py-4">Remark</th>
                        <th className="px-6 py-4">Current Status</th>
                        <th className="px-6 py-4">Created Time</th>
                        <th className="px-6 py-4">Completion Status</th>
                        <th className="px-6 py-4 text-center">Actions</th>
                    </tr>
                </thead>
                <tbody className="bg-white divide-y divide-slate-100">
                    {requests.map((request) => (
                        <tr
                            key={request.requestId}
                            className="hover:bg-slate-50 transition-colors group"
                        >
                            <td className="px-6 py-4 font-medium text-slate-900 flex items-center gap-2">
                                <Hash className="w-4 h-4 text-slate-400" />
                                {request.requestId}
                            </td>
                            <td className="px-6 py-4 font-mono text-slate-600">
                                {request.cardNumber}
                            </td>
                            <td className="px-6 py-4 text-slate-600">
                                <div className="flex items-center gap-2">
                                    <Tag className="w-4 h-4 text-slate-400" />
                                    {request.requestReasonCode}
                                </div>
                            </td>
                            <td className="px-6 py-4 text-slate-600 truncate max-w-[200px]" title={request.remark}>
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
                                    "inline-flex items-center gap-1.5 px-2.5 py-1 rounded-full text-xs font-medium",
                                    request.completionStatus === 'COMPLETED' || request.completionStatus === 'SUCCESS' ? 'bg-green-100 text-green-800' :
                                        request.completionStatus === 'FAILED' ? 'bg-red-100 text-red-800' :
                                            'bg-amber-100 text-amber-800'
                                )}>
                                    {request.completionStatus === 'COMPLETED' || request.completionStatus === 'SUCCESS' ? <CheckCircle2 className="w-3 h-3" /> :
                                        request.completionStatus === 'FAILED' ? <XCircle className="w-3 h-3" /> :
                                            <Clock className="w-3 h-3" />}
                                    {request.completionStatus || 'PENDING'}
                                </span>
                            </td>
                            <td className="px-6 py-4 text-center">
                                <button
                                    onClick={() => onChangeStatus(request)}
                                    className="p-2 text-slate-400 hover:text-blue-600 hover:bg-blue-50 rounded-full transition-all"
                                    title="Change Status"
                                >
                                    <Activity className="w-4 h-4" />
                                </button>
                            </td>
                        </tr>
                    ))}
                    {requests.length === 0 && (
                        <tr>
                            <td colSpan={8} className="px-6 py-12 text-center text-slate-400">
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
