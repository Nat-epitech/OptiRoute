<script setup lang="ts">
defineProps<{
  show: boolean
  title?: string
  message?: string
  loading?: boolean
}>()

defineEmits<{
  close: []
  confirm: []
}>()
</script>

<template>
  <div
    v-if="show"
    class="fixed inset-0 z-50 flex items-center justify-center bg-black/50"
    @click.self="$emit('close')"
  >
    <div class="w-full max-w-md rounded-2xl bg-white p-6 shadow-xl">
      <h2 class="mb-2 text-xl font-semibold text-gray-800">
        {{ title ?? 'Confirmer la suppression' }}
      </h2>

      <p class="mb-6 text-gray-600">
        {{ message ?? 'Êtes-vous sûr de vouloir supprimer cet élément ?' }}
      </p>

      <div class="flex justify-end gap-3">
        <button
          type="button"
          class="rounded-lg border px-4 py-2 text-gray-700 hover:bg-gray-50"
          :disabled="loading"
          @click="$emit('close')"
        >
          Annuler
        </button>

        <button
          type="button"
          class="rounded-lg bg-red-600 px-4 py-2 text-white hover:bg-red-700 disabled:opacity-50"
          :disabled="loading"
          @click="$emit('confirm')"
        >
          {{ loading ? 'Suppression...' : 'Supprimer' }}
        </button>
      </div>
    </div>
  </div>
</template>