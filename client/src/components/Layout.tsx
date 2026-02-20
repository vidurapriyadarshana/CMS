import { Link, Outlet } from 'react-router-dom'

const Layout = () => {
    return (
        <div className="flex h-screen bg-slate-50 text-slate-900 selection:bg-blue-500/20">
            <aside className="w-64 bg-white border-r border-slate-200 flex flex-col relative overflow-hidden">
                <div className="p-8 border-b border-slate-100 flex items-center gap-3 relative z-10">
                    <div className="w-8 h-8 rounded-lg bg-blue-600 flex items-center justify-center shadow-sm">
                        <span className="text-white font-bold text-lg leading-none">C</span>
                    </div>
                    <h2 className="text-xl font-bold tracking-tight text-slate-900">Card App</h2>
                </div>
                <nav className="mt-8 px-4 flex-1 space-y-2 relative z-10">
                    <Link to="/" className="block py-2.5 px-4 rounded-lg transition-colors font-medium text-slate-600 hover:bg-blue-50 hover:text-blue-700">
                        Manage Cards
                    </Link>
                    <Link to="/card-request" className="block py-2.5 px-4 rounded-lg transition-colors font-medium text-slate-600 hover:bg-blue-50 hover:text-blue-700">
                        Card Request
                    </Link>
                    <Link to="/confirm-card-requests" className="block py-2.5 px-4 rounded-lg transition-colors font-medium text-slate-600 hover:bg-blue-50 hover:text-blue-700">
                        Confirm Requests
                    </Link>
                </nav>
                <div className="p-6 text-xs text-slate-400 font-medium text-center border-t border-slate-100">
                    Epic Lanka System v1.0.0
                </div>
            </aside>
            <main className="flex-1 p-10 overflow-auto relative">
                <Outlet />
            </main>
        </div>
    )
}

export default Layout
