import { Link, Outlet } from 'react-router-dom'

const Layout = () => {
    return (
        <div className="flex h-screen bg-gray-100 dark:bg-gray-900">
            <aside className="w-64 bg-white dark:bg-gray-800 border-r border-gray-200 dark:border-gray-700">
                <div className="p-6">
                    <h2 className="text-2xl font-bold text-gray-800 dark:text-white">Card App</h2>
                </div>
                <nav className="mt-6">
                    <Link to="/" className="block py-2.5 px-4 rounded transition duration-200 hover:bg-gray-200 dark:hover:bg-gray-700 hover:text-gray-900 dark:hover:text-white">
                        Home
                    </Link>
                    <Link to="/manage-cards" className="block py-2.5 px-4 rounded transition duration-200 hover:bg-gray-200 dark:hover:bg-gray-700 hover:text-gray-900 dark:hover:text-white">
                        Manage Cards
                    </Link>
                    <Link to="/card-request" className="block py-2.5 px-4 rounded transition duration-200 hover:bg-gray-200 dark:hover:bg-gray-700 hover:text-gray-900 dark:hover:text-white">
                        Card Request
                    </Link>
                    <Link to="/confirm-card-requests" className="block py-2.5 px-4 rounded transition duration-200 hover:bg-gray-200 dark:hover:bg-gray-700 hover:text-gray-900 dark:hover:text-white">
                        Confirm Requests
                    </Link>
                </nav>
            </aside>
            <main className="flex-1 p-10 overflow-auto">
                <Outlet />
            </main>
        </div>
    )
}

export default Layout
