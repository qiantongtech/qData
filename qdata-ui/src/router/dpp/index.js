import integratio from './integratio/integratio'
import developTask from './task/developTask'
import integratioTask from './task/integratioTask'
import asset from './asset/asset'

// 合并所有 dpp 模块下的路由
const dppRouter = [
    ...integratio,
    ...developTask,
    ...integratioTask,
    ...asset
]

export default dppRouter