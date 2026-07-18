import { readonly, ref } from 'vue'

export type NotificationType =
    | 'success'
    | 'error'
    | 'warning'
    | 'info'

export interface AppNotification {
    id: number
    type: NotificationType
    title: string
    message?: string
}

const notifications = ref<AppNotification[]>([])

let nextId = 1

export function useNotification() {
    function showNotification(type: NotificationType, title: string, message?: string, duration = 4000) {
        const id = nextId++

        notifications.value.push({ id, type, title, message })

        if (duration > 0) {
            window.setTimeout(() => { removeNotification(id) }, duration)
        }

        return id
    }

    function removeNotification(id: number) {
        notifications.value = notifications.value.filter(
            notification => notification.id !== id
        )
    }

    function success(title: string, message?: string, duration?: number) {
        return showNotification('success', title, message, duration)
    }

    function error(title: string, message?: string, duration = 6000) {
        return showNotification('error', title, message, duration)
    }

    function warning(title: string, message?: string, duration?: number) {
        return showNotification('warning', title, message, duration)
    }

    function info(title: string, message?: string, duration?: number) {
        return showNotification('info', title, message, duration)
    }

    return {
        notifications: readonly(notifications), removeNotification, success, error, warning, info
    }
}