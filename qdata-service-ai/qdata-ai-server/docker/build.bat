@rem Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
@rem
@rem This file is part of qData Data Middle Platform (Open Source Edition).
@rem
@rem qData is licensed under Apache License 2.0 with additional qData terms.
@rem You may use qData for commercial purposes, but you may not remove, hide,
@rem modify, or replace the qData logo, copyright notices, license notices,
@rem or attribution information without a separate commercial license.
@rem
@rem White-label use, OEM distribution, rebranding, or presenting qData as
@rem another product requires separate commercial authorization from
@rem Jiangsu Qiantong Technology Co., Ltd.
@rem
@rem Business License: https://docs.qdata.tech/business/policy.html
@rem See the LICENSE file in the project root for full license information.

docker buildx build --platform linux/amd64,linux/arm64 --no-cache --provenance=false --sbom=false --push -t crpi-kf13onfj0v8f6jax.cn-shanghai.personal.cr.aliyuncs.com/qiantongkeji/qdata-service-ai-ce:1.6.9 .
