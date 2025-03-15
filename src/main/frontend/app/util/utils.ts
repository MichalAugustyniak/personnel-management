export function throwIfUndefined<T>(value: T | undefined, error: Error): T {
    if (!value) {
        throw error;
    }
    return value;
}

export function trimString(value: string | undefined): string | undefined {
    if (!value) {
        return undefined;
    }
    return value.trim();
}

export function trimOrThrow(value: string | undefined, error: Error): string {
    if (!value) {
        throw error;
    }
    return value.trim();
}
