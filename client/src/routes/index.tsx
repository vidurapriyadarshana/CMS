import { useRoutes } from 'react-router-dom'
import Layout from '@/components/Layout'
import ManageCards from '@/pages/ManageCards'
import CardRequest from '@/pages/CardRequest'
import ConfirmCardRequests from '@/pages/ConfirmCardRequests'
import Reports from '@/pages/Reports'

export const AppRoutes = () => {
    const routes = useRoutes([
        {
            path: '/',
            element: <Layout />,
            children: [
                { path: 'card-request', element: <CardRequest /> },
                { path: 'confirm-card-requests', element: <ConfirmCardRequests /> },
                { path: 'reports', element: <Reports /> },
                { path: '/', element: <ManageCards /> }, // Default redirect or home page
            ],
        },
    ])
    return routes
}
