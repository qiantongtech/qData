<template>
    <div class="app-container" ref="app-container">
        <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
            <el-tab-pane label="项目成员" name="first">
                <User ref="userRef"></User>
            </el-tab-pane>
            <el-tab-pane label="项目角色" name="second">
                <Role ref="roleRef"></Role>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script setup name="index">
import { ref } from 'vue';
import Role from '@/views/dpp/setting/projectUserRel/role.vue';
import User from '@/views/dpp/setting/projectUserRel/user.vue';
const activeName = ref('first');
</script>

<style scoped lang="less">
::v-deep(.el-tabs__header) {
    margin: 0 0 0px;
    padding: 0px 15px 0px 15px;
    position: relative;
    background-color: #fff;
}
</style>
