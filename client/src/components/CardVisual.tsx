
import React from 'react';
import type { CardResponse } from '../types/card';
import { cn } from '../lib/utils'; // Assuming cn exists or I should use a simple utility. 
// If utils doesn't exist, I'll inline the classes or create it. 
// Given package.json has clsx/tailwind-merge, let's assume standard shadcn utils structure or create inline.
// I'll check if lib/utils exists first? No, I'll just write safe code.

const CardVisual: React.FC<{ card: CardResponse; className?: string }> = ({ card, className }) => {
    // Basic gradient background for "Premium" look
    return (
        <div className={cn("relative w-96 h-56 rounded-2xl bg-gradient-to-br from-blue-700 via-blue-800 to-blue-950 text-white shadow-xl p-6 flex flex-col justify-between border border-blue-500/30 overflow-hidden", className)}>
            {/* Background Texture/Effect */}
            <div className="absolute inset-0 bg-[url('https://grainy-gradients.vercel.app/noise.svg')] opacity-20 rounded-2xl pointer-events-none"></div>

            <div className="flex justify-between items-start z-10">
                <div className="flex flex-col">
                    <h2 className="text-xl font-bold tracking-wider font-sans">Epic Lanka</h2>
                    <span className="text-xs text-white/50 uppercase tracking-widest mt-1">Platinum Credit</span>
                </div>
                {/* Chip Icon Simulation */}
                <div className="w-12 h-9 bg-gradient-to-r from-yellow-200 to-yellow-500 rounded-md border border-yellow-600 shadow-inner flex items-center justify-center relative overflow-hidden">
                    <div className="absolute w-full h-[1px] bg-yellow-700 top-1/2 -translate-y-1/2"></div>
                    <div className="absolute h-full w-[1px] bg-yellow-700 left-1/2 -translate-x-1/2"></div>
                    <div className="w-8 h-6 border border-yellow-700 rounded-sm"></div>
                </div>
            </div>

            <div className="z-10 mt-4">
                <div className="flex items-center justify-between w-full mt-2">
                    <span className="text-xl font-mono tracking-wider text-white drop-shadow-md">
                        {card.cardNumber.substring(0, 4)}
                    </span>
                    <span className="text-xl font-mono tracking-wider text-white drop-shadow-md">
                        ****
                    </span>
                    <span className="text-xl font-mono tracking-wider text-white drop-shadow-md">
                        ****
                    </span>
                    <span className="text-xl font-mono tracking-wider text-white drop-shadow-md">
                        {card.cardNumber.substring(card.cardNumber.length - 4)}
                    </span>
                </div>
            </div>

            <div className="flex justify-between items-end z-10">
                <div className="flex flex-col">
                    <span className="text-[10px] text-white/60 uppercase tracking-wider">Card Holder</span>
                    <span className="font-medium tracking-wide">VALUED CUSTOMER</span>
                </div>
                <div className="flex flex-col items-end">
                    <span className="text-[10px] text-white/60 uppercase tracking-wider">Expires</span>
                    <span className="font-mono font-medium">{card.expireDate}</span>
                </div>
            </div>

            {/* Visa/Mastercard Logo Area (Placeholder text for now) */}
            <div className="absolute bottom-6 right-6 opacity-80 z-10 hidden">
                <div className="flex -space-x-2">
                    <div className="w-8 h-8 rounded-full bg-red-500/80"></div>
                    <div className="w-8 h-8 rounded-full bg-orange-400/80"></div>
                </div>
            </div>
        </div>
    );
};

export default CardVisual;
