
export interface CardResponse {
    cardNumber: string;
    encryptedCardNumber: string;
    expireDate: string; // "MM/YY"
    cardStatus: string; // e.g., "IACT", "ACTIVE"
    creditLimit: number;
    cashLimit: number;
    availableCreditLimit: number;
    availableCashLimit: number;
    lastUpdateTime: string; // ISO Date string
}

export interface UpdateCardRequest {
    expireDate: string; // "MM/YY"
    creditLimit: number;
    cashLimit: number;
    availableCreditLimit: number;
    availableCashLimit: number;
}

export interface CommonResponse<T> {
    code: number;
    status: string;
    data: T;
    message?: string;
}
