/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://docs.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.da.service.datasource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.core.PageResult;
import tech.qiantong.qdata.common.database.exception.DataQueryException;
import tech.qiantong.qdata.module.da.dal.dataobject.asset.DaAssetDO;
import tech.qiantong.qdata.module.da.dal.mapper.asset.DaAssetMapper;
import tech.qiantong.qdata.module.da.service.asset.IDaAssetService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import javax.annotation.Resource;
import java.util.*;

/** Applies asset-field sensitivity rules to SQL results, including aliased columns. */
@Component
public class QuerySensitivityMasker {
    @Resource
    private DaAssetMapper assetMapper;
    @Resource
    private IDaAssetService assetService;

    public void mask(Long datasourceId, PageResult<Map<String, Object>> result) {
        if (result == null || result.getData() == null || result.getData().isEmpty()
                || result.getColumnOrigins() == null || result.getColumnOrigins().isEmpty()) return;

        String sourceTable = null;
        Map<String, String> labels = new LinkedHashMap<>();
        // Identify the source table and map result labels to original column names.
        for (Map.Entry<String, DbColumn> entry : result.getColumnOrigins().entrySet()) {
            DbColumn origin = entry.getValue();
            if (origin == null || origin.getTableName() == null || origin.getTableName().isEmpty()) continue;
            if (sourceTable != null && !sameTable(sourceTable, origin.getTableName())) {
                throw new DataQueryException("Only single-table query results are supported");
            }
            sourceTable = origin.getTableName();
            if (origin.getColName() != null && !origin.getColName().isEmpty()) {
                labels.put(entry.getKey(), origin.getColName());
            }
        }
        if (labels.isEmpty()) return;

        List<DaAssetDO> assets = assetMapper.selectList(Wrappers.<DaAssetDO>lambdaQuery()
                .eq(DaAssetDO::getDatasourceId, datasourceId));
        DaAssetDO matchedAsset = null;
        // Find the asset for the source table and stop at the first match.
        for (DaAssetDO asset : assets) {
            if (sameTable(asset.getTableName(), sourceTable)) {
                matchedAsset = asset;
                break;
            }
        }
        if (matchedAsset == null) return;

        List<Map<String, Object>> rows = result.getData();
        List<Map<String, Object>> sourceRows = new ArrayList<>(rows.size());
        // Build rows keyed by original column names for the masking service.
        for (Map<String, Object> row : rows) {
            Map<String, Object> source = new LinkedHashMap<>();
            row.forEach((label, value) -> {
                String column = labels.get(label.toUpperCase(Locale.ROOT));
                if (column != null) source.put(column, value);
            });
            sourceRows.add(source);
        }

        List<Map<String, Object>> masked = assetService.dataMasking(matchedAsset.getId(), sourceRows);
        // Copy masked values back into the corresponding rows without changing result labels.
        for (int index = 0; index < rows.size(); index++) {
            Map<String, Object> maskedRow = masked.get(index);
            rows.get(index).replaceAll((label, value) -> {
                String column = labels.get(label.toUpperCase(Locale.ROOT));
                return column == null ? value : maskedRow.get(column);
            });
        }
    }

    private static boolean sameTable(String assetTable, String jdbcTable) {
        if (assetTable == null || jdbcTable == null || jdbcTable.isEmpty()) return false;
        return assetTable.equalsIgnoreCase(jdbcTable);
    }
}
