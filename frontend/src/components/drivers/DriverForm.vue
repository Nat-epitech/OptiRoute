<script setup lang="ts">
import type { DriverFormData } from "@/models/driver/Driver"

const form = defineModel<DriverFormData>({
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
    <div class="space-y-6">
        <section class="space-y-4">
            <h3 class="text-sm font-semibold uppercase tracking-wide text-slate-500">
                Identification
            </h3>

            <div class="grid grid-cols-1 gap-4 sm:grid-cols-2">
                <div>
                    <label class="mb-1 block text-sm font-medium text-slate-700">
                        Prénom
                    </label>

                    <input v-model="form.firstName" type="text" required maxlength="100" :disabled="disabled"
                        class="w-full rounded-xl border border-slate-300 px-4 py-3 outline-none transition focus:border-blue-500 disabled:cursor-not-allowed disabled:bg-slate-100" />
                </div>

                <div>
                    <label class="mb-1 block text-sm font-medium text-slate-700">
                        Nom
                    </label>

                    <input v-model="form.lastName" type="text" required maxlength="100" :disabled="disabled"
                        class="w-full rounded-xl border border-slate-300 px-4 py-3 outline-none transition focus:border-blue-500 disabled:cursor-not-allowed disabled:bg-slate-100" />
                </div>
            </div>

            <div>
                <label class="mb-1 block text-sm font-medium text-slate-700">
                    Numéro de téléphone
                </label>

                <input v-model="form.phoneNumber" type="tel" maxlength="30" :disabled="disabled"
                    class="w-full rounded-xl border border-slate-300 px-4 py-3 outline-none transition focus:border-blue-500 disabled:cursor-not-allowed disabled:bg-slate-100" />
            </div>
        </section>

        <section class="space-y-4">
            <h3 class="text-sm font-semibold uppercase tracking-wide text-slate-500">
                Coût et temps de travail
            </h3>

            <div class="grid grid-cols-1 gap-4 sm:grid-cols-2">
                <div>
                    <label class="mb-1 block text-sm font-medium text-slate-700">
                        Coût annuel
                    </label>

                    <input v-model.number="form.annualSalary" type="number" min="0" step="100" :disabled="disabled"
                        class="w-full rounded-xl border border-slate-300 px-4 py-3 outline-none transition focus:border-blue-500 disabled:cursor-not-allowed disabled:bg-slate-100" />

                    <p class="mt-1 text-xs text-slate-500">
                        Euros par an
                    </p>

                </div>

                <div>
                    <label class="mb-1 block text-sm font-medium text-slate-700">
                        Temps de travail mensuel
                    </label>

                    <input v-model.number="form.monthlyWorkingHours" type="number" min="0" step="1" :disabled="disabled"
                        class="w-full rounded-xl border border-slate-300 px-4 py-3 outline-none transition focus:border-blue-500 disabled:cursor-not-allowed disabled:bg-slate-100" />

                    <p class="mt-1 text-xs text-slate-500">
                        Heures par mois
                    </p>
                </div>
            </div>
        </section>
    </div>
</template>