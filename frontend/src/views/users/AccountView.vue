<script setup lang="ts">
import { onMounted, ref } from "vue"
import { LockKeyhole, UserRound } from "lucide-vue-next"

import { getCurrentUser } from "@/api/userApi"
import { getApiErrorMessage } from "@/api/utils"
import type { User } from "@/models/User"

const user = ref<User | null>(null)
const loading = ref(true)
const error = ref<string | null>(null)

const loadCurrentUser = async () => {
	try {
		loading.value = true
		error.value = null
		user.value = await getCurrentUser()
	} catch (err: unknown) {
		error.value = getApiErrorMessage(err, "Impossible de charger les informations du compte.")
	} finally {
		loading.value = false
	}
}

onMounted(loadCurrentUser)
</script>

<template>
	<div class="w-full max-w-3xl">
		<div class="mb-6 flex items-center gap-3">
			<div class="flex h-11 w-11 items-center justify-center rounded-xl bg-blue-100 text-blue-600">
				<UserRound class="h-6 w-6" />
			</div>

			<div>
				<h1 class="text-3xl font-bold text-gray-800">
					Mon compte
				</h1>

				<p class="mt-1 text-sm text-gray-500">
					Informations personnelles
				</p>
			</div>
		</div>

		<div v-if="loading" class="rounded-2xl bg-white p-8 text-center text-sm text-gray-500 shadow">
			Chargement des informations...
		</div>

		<div v-else-if="error" class="rounded-2xl border border-red-200 bg-red-50 p-6 text-sm text-red-700 shadow">
			{{ error }}
		</div>

		<section v-else-if="user" class="rounded-2xl bg-white p-6 shadow">
			<div class="grid grid-cols-1 gap-5 sm:grid-cols-2">
				<div class="sm:col-span-2">
					<label class="mb-1 block text-sm font-medium text-slate-700">
						Email
					</label>

					<input :value="user.email" type="email" readonly
						class="w-full rounded-xl border border-slate-300 bg-slate-50 px-4 py-3 text-slate-700 outline-none" />
				</div>

				<div>
					<label class="mb-1 block text-sm font-medium text-slate-700">
						Prénom
					</label>

					<input :value="user.firstName" type="text" readonly
						class="w-full rounded-xl border border-slate-300 bg-slate-50 px-4 py-3 text-slate-700 outline-none" />
				</div>

				<div>
					<label class="mb-1 block text-sm font-medium text-slate-700">
						Nom
					</label>

					<input :value="user.lastName" type="text" readonly
						class="w-full rounded-xl border border-slate-300 bg-slate-50 px-4 py-3 text-slate-700 outline-none" />
				</div>
			</div>
		</section>
	</div>
</template>
