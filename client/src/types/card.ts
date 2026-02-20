
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

export interface CardRequest {
    cardNumber: string;
    expireDate: string; // "MM/YY"
    creditLimit: number;
    cashLimit: number;
    availableCreditLimit: number;
    availableCashLimit: number;
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

export interface CardRequestType {
    code: string;
    description: string;
}

export interface SendCardRequestPayload {
    requestReasonCode: string;
    remark: string;
    cardNumber: string;
    status: string;
}

export interface StatusType {
    statusCode: string;
    description: string;
}

export interface CardRequestData {
    requestId: number;
    requestReasonCode: string;
    remark: string;
    cardNumber: string;
    encryptedCardNumber: string;
    status: string;
    createdTime: string; // ISO Date String
    completionStatus: string;
}
