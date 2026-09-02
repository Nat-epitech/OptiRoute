<script setup lang="ts">
export type TrailerTypeFormData = {
    label: string
    description: string | null
}

const form = defineModel<TrailerTypeFormData>({
    required: true,
})

withDefaults(
    defineProps<{ disabled?: boolean }>(),
    {
        disabled: false,
    },
)
</script>

<template>
    <div class="space-y-4">
        <div>
            <label class="mb-1 block text-sm font-medium text-slate-700">
                Libellé
            </label>

            <input v-model="form.label" type="text" required maxlength="100" :disabled="disabled"
                placeholder="Ex. Fourgon"
                class="w-full rounded-xl border border-slate-300 px-4 py-3 outline-none transition focus:border-blue-500 disabled:cursor-not-allowed disabled:bg-slate-100" />
        </div>

        <div>
            <label class="mb-1 block text-sm font-medium text-slate-700">
                Description
            </label>

            <textarea v-model="form.description" rows="4" maxlength="255" :disabled="disabled"
                placeholder="Description du type de semi-remorque"
                class="w-full resize-none rounded-xl border border-slate-300 px-4 py-3 outline-none transition focus:border-blue-500 disabled:cursor-not-allowed disabled:bg-slate-100" />
        </div>
    </div>
</template>
