<script setup>
import { ref } from 'vue';

</script>

<template>
  <div class="home-view">
    <BookmarkIcon style="width: 1rem; height: 1rem; vertical-align: middle;" />
    <h1>
      Welcome to the Home View
    </h1>
    
    <Select>
        <template #dropdownicon>
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
                <g id="chevron-down">
                    <path d="M12,15.25a.74.74,0,0,1-.53-.22l-5-5A.75.75,0,0,1,7.53,9L12,13.44,16.47,9A.75.75,0,0,1,17.53,10l-5,5A.74.74,0,0,1,12,15.25Z"/>
                </g>
            </svg>
        </template>
    </Select>

    <p>This is the main landing page of the application.
      <i class="pi pi-check" style="font-size: 1rem"></i>
    </p>
    <Form v-slot="$form" :initialValues :resolver @submit="onFormSubmit" class="grid lg:grid-cols-2 gap-4 w-full">
      <div class="flex flex-col justify-center items-center gap-4">
          <InputText name="username" type="text" placeholder="Username" class="w-full sm:w-56" />
          <Button type="submit" severity="secondary" label="Submit" class="w-full sm:w-56" />
      </div>
      <Fieldset legend="Form States" class="h-80 overflow-auto">
          <pre class="whitespace-pre-wrap">{{ $form }}</pre>
      </Fieldset>
  </Form>
  </div>
</template>

<style scoped>
.home-view {
  padding: 20px;
}
</style>