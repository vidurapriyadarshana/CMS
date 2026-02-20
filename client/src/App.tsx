import { BrowserRouter } from 'react-router-dom'
import { AppRoutes } from './routes'

import { Toaster } from 'sonner';

function App() {
  return (
    <BrowserRouter>
      <AppRoutes />
      <Toaster position="bottom-right" richColors />
    </BrowserRouter>
  )
}

export default App
