<script setup>
import { ref, watch, onMounted, onBeforeUnmount } from "vue";

const props = defineProps({
    graph: Object, // 传入 graph 实例
});

const visible = ref(false);
const position = ref({ x: 0, y: 0 });
const node = ref(null);
const edge = ref(null);
const type = ref(0);

const menuItems = ref([]);

const createMenuItems = () => {
    if (type.value === 0) {
        menuItems.value = [
            { label: "编辑节点", action: "edit" },
            { label: "删除节点", action: "delete" },
            { label: "显示输入字段", action: "showInputs" },
            { label: "显示输出字段", action: "showOutputs" },
        ];
    } else if (type.value === 1) {
        menuItems.value = [{ label: "删除连接线", action: "delete" }];
    } else {
        menuItems.value = [];
    }
};

const getAllChildNodes = (node, graph) => {
    const allChildren = [];
    function dfs(n) {
        const children = graph.getChildren(n) || [];
        children.forEach((child) => {
            allChildren.push(child);
            dfs(child);
        });
    }
    dfs(node);
    return allChildren;
};

const showContextMenu = ({ x, y, node: n, edge: e, type: t }) => {
    position.value = { x, y };
    node.value = n;
    edge.value = e;
    type.value = t;
    createMenuItems();
    visible.value = true;
};

const hideContextMenu = () => {
    visible.value = false;
};

const handleClick = (action) => {
    if (!props.graph) {
        console.warn("请传入 graph 实例");
        hideContextMenu();
        return;
    }

    if (type.value === 0 && node.value) {
        // 节点操作
        if (action === "delete") {
            const childNodes = getAllChildNodes(node.value, props.graph);
            childNodes.forEach((childNode) => {
                if (childNode.data?.taskParams) {
                    childNode.data.taskParams.tableFields = [];
                    childNode.data = { ...childNode.data };
                }
            });
            props.graph.removeNode(node.value);
        } else if (action === "edit") {
            // 编辑逻辑（自己实现）
            console.log("编辑节点", node.value);
        } else if (action === "showInputs") {
            console.log("显示输入字段", node.value);
        } else if (action === "showOutputs") {
            console.log("显示输出字段", node.value);
        }
    } else if (type.value === 1 && edge.value) {
        // 边操作
        if (action === "delete") {
            const sourceNode = edge.value.getSourceCell();
            const childNodes = getAllChildNodes(sourceNode, props.graph);
            childNodes.forEach((childNode) => {
                if (childNode.data?.taskParams) {
                    childNode.data.taskParams.tableFields = [];
                    childNode.data = { ...childNode.data };
                }
            });
            props.graph.removeEdge(edge.value);
        }
    }

    hideContextMenu();
};

const onClickOutside = (e) => {
    if (!e.target.closest(".div-menu-container")) {
        hideContextMenu();
    }
};

onMounted(() => {
    document.body.addEventListener("click", onClickOutside);
});

onBeforeUnmount(() => {
    document.body.removeEventListener("click", onClickOutside);
});

defineExpose({
    showContextMenu,
    hideContextMenu,
});
</script>
