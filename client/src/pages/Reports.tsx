import { FileText, FileSpreadsheet } from 'lucide-react';

const Reports = () => {
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

                    <div className="flex gap-4">
                        <button
                            onClick={() => window.open('/reports/cards/pdf', '_blank')}
                            className="flex-1 flex items-center justify-center gap-2 px-4 py-3 bg-rose-50 text-rose-700 hover:bg-rose-100 rounded-xl font-bold transition-colors shadow-sm focus:outline-none focus:ring-2 focus:ring-rose-500 focus:ring-offset-2 active:scale-95"
                            title="Export Cards to PDF"
                        >
                            <FileText className="w-5 h-5" />
                            Download PDF
                        </button>
                        <button
                            onClick={() => window.open('/reports/cards/csv', '_blank')}
                            className="flex-1 flex items-center justify-center gap-2 px-4 py-3 bg-emerald-50 text-emerald-700 hover:bg-emerald-100 rounded-xl font-bold transition-colors shadow-sm focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:ring-offset-2 active:scale-95"
                            title="Export Cards to CSV"
                        >
                            <FileSpreadsheet className="w-5 h-5" />
                            Download CSV
                        </button>
                    </div>
                </div>

                {/* Card Requests Reports */}
                <div className="bg-white p-8 rounded-2xl border border-slate-200 shadow-sm transition-shadow hover:shadow-md">
                    <h2 className="text-xl font-bold text-slate-800 mb-2">Card Request Reports</h2>
                    <p className="text-slate-500 text-sm mb-6">Export a list of all incoming card requests and their statuses.</p>

                    <div className="flex gap-4">
                        <button
                            onClick={() => window.open('/reports/card-requests/pdf', '_blank')}
                            className="flex-1 flex items-center justify-center gap-2 px-4 py-3 bg-rose-50 text-rose-700 hover:bg-rose-100 rounded-xl font-bold transition-colors shadow-sm focus:outline-none focus:ring-2 focus:ring-rose-500 focus:ring-offset-2 active:scale-95"
                            title="Export Card Requests to PDF"
                        >
                            <FileText className="w-5 h-5" />
                            Download PDF
                        </button>
                        <button
                            onClick={() => window.open('/reports/card-requests/csv', '_blank')}
                            className="flex-1 flex items-center justify-center gap-2 px-4 py-3 bg-emerald-50 text-emerald-700 hover:bg-emerald-100 rounded-xl font-bold transition-colors shadow-sm focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:ring-offset-2 active:scale-95"
                            title="Export Card Requests to CSV"
                        >
                            <FileSpreadsheet className="w-5 h-5" />
                            Download CSV
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Reports;
