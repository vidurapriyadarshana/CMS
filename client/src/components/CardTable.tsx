
import React from 'react';
import type { CardResponse } from '../types/card';
import { Calendar, CreditCard } from 'lucide-react';

interface CardTableProps {
    cards: CardResponse[];
    onCardClick: (card: CardResponse) => void;
}

const CardTable: React.FC<CardTableProps> = ({ cards, onCardClick }) => {
    return (
        <div className="overflow-x-auto rounded-xl border border-slate-200 shadow-sm bg-white">
            <table className="w-full text-sm text-left">
                <thead className="bg-slate-50 text-slate-500 font-medium uppercase text-xs border-b border-slate-200">
                    <tr>
                        <th className="px-6 py-4">Card Number</th>
                        <th className="px-6 py-4">Expiry</th>
                        <th className="px-6 py-4">Status</th>
                        <th className="px-6 py-4 text-right">Credit Limit</th>
                        <th className="px-6 py-4 text-right">Cash Limit</th>
                        <th className="px-6 py-4 text-right">Avail. Credit</th>
                        <th className="px-6 py-4 text-right">Avail. Cash</th>
                        <th className="px-6 py-4 text-right">Last Updated</th>
                    </tr>
                </thead>
                <tbody className="divide-y divide-slate-100">
                    {cards.map((card) => (
                        <tr
                            key={card.encryptedCardNumber}
                            onClick={() => onCardClick(card)}
                            className="hover:bg-slate-50 transition-colors cursor-pointer group"
                        >
                            <td className="px-6 py-4 font-medium text-slate-900 group-hover:text-blue-600 flex items-center gap-2">
                                <CreditCard className="w-4 h-4 text-slate-400" />
                                {card.cardNumber}
                            </td>
                            <td className="px-6 py-4 text-slate-600">
                                <div className="flex items-center gap-2">
                                    <Calendar className="w-3 h-3 text-slate-300" />
                                    {card.expireDate}
                                </div>
                            </td>
                            <td className="px-6 py-4">
                                <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${card.cardStatus === 'ACTIVE'
                                    ? 'bg-green-100 text-green-800'
                                    : 'bg-slate-100 text-slate-800'
                                    }`}>
                                    {card.cardStatus}
                                </span>
                            </td>
                            <td className="px-6 py-4 text-right font-mono text-slate-600">
                                {card.creditLimit?.toLocaleString() ?? 'N/A'}
                            </td>
                            <td className="px-6 py-4 text-right font-mono text-slate-600">
                                {card.cashLimit?.toLocaleString() ?? 'N/A'}
                            </td>
                            <td className="px-6 py-4 text-right font-mono font-medium text-slate-900">
                                {card.availableCreditLimit?.toLocaleString() ?? 'N/A'}
                            </td>
                            <td className="px-6 py-4 text-right font-mono font-medium text-slate-900">
                                {card.availableCashLimit?.toLocaleString() ?? 'N/A'}
                            </td>
                            <td className="px-6 py-4 text-right text-slate-400 text-xs">
                                {card.lastUpdateTime ? new Date(card.lastUpdateTime).toLocaleDateString() : 'N/A'}
                            </td>
                        </tr>
                    ))}
                    {cards.length === 0 && (
                        <tr>
                            <td colSpan={8} className="px-6 py-12 text-center text-slate-400">
                                No cards found.
                            </td>
                        </tr>
                    )}
                </tbody>
            </table>
        </div>
    );
};

export default CardTable;
