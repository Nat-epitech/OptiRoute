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
  <Teleport to="body">
    <Transition enter-active-class="transition duration-200 ease-out" enter-from-class="opacity-0"
      enter-to-class="opacity-100" leave-active-class="transition duration-150 ease-in" leave-from-class="opacity-100"
      leave-to-class="opacity-0">
      <div v-if="show" class="fixed inset-0 z-[100] flex items-center justify-center p-4" @click.self="$emit('close')">
        <div class="absolute inset-0 bg-slate-950/50 backdrop-blur-sm" />

        <div class="relative z-10 w-full max-w-md rounded-2xl bg-white p-6 shadow-2xl" role="dialog" aria-modal="true"
          @click.stop>
          <h2 class="mb-2 text-xl font-semibold text-slate-900">
            {{ title ?? "Confirmer la suppression" }}
          </h2>

          <p class="mb-6 text-slate-600">
            {{ message ?? "Êtes-vous sûr de vouloir supprimer cet élément ?" }}
          </p>

          <div class="flex justify-end gap-3">
            <button type="button"
              class="rounded-xl border border-slate-300 px-4 py-2 text-slate-700 transition hover:bg-slate-50 disabled:cursor-not-allowed disabled:opacity-50"
              :disabled="loading" @click="$emit('close')">
              Annuler
            </button>

            <button type="button"
              class="rounded-xl bg-red-600 px-4 py-2 text-white transition hover:bg-red-700 disabled:cursor-not-allowed disabled:opacity-50"
              :disabled="loading" @click="$emit('confirm')">
              {{ loading ? "Suppression..." : "Supprimer" }}
            </button>
          </div>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>