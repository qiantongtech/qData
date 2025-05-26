

export const useProjectStore = defineStore('project', {
    state: () => ({
        project: {}
    }),
    actions: {
        // 设置整个项目对象
        setProject(newProject) {
            this.project = newProject
        },
        // 设置项目中的某个属性
        setProjectField(field, value) {
            this.project[field] = value
        }
    }
})
