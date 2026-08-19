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
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

import packageInfo from '../../package.json'

const logo = [
  '           ____            _            ',
  '    __ _  |  _ \\    __ _  | |_    __ _  ',
  '   / _` | | | | |  / _` | | __|  / _` | ',
  '  | (_| | | |_| | | (_| | | |_  | (_| | ',
  '   \\__, | |____/   \\__,_|  \\__|  \\__,_| ',
  '      |_|                                '
].join('\n')

const licenseContent = {
  intro: `qData is made available under the Apache License 2.0, subject to the following additional conditions specific to qData:
For the purpose of this License, "qData" refers to the entire software project, including but not limited to its frontend, backend, API, and related documentation in whole or in part.
For the purpose of this License, "Producer" refers to Jiangsu Qiantong Technology Co., Ltd.`,
  commercialTitle: '1. You may use qData for commercial purposes, provided that you do not remove, hide, or modify the qData logo, copyright notices, license notices, or attribution information displayed in the qData.',
  commercialDetail: 'If you wish to remove, hide, obscure, replace, or modify such logo, copyright notices, license notices, or attribution information, including but not limited to white-label use, OEM distribution, rebranding, or presenting qData as another product, you must obtain a separate commercial license from the Producer.',
  contributorTitle: '2. As a contributor, you should agree that:',
  contributorDetail: `a. The Producer can adjust the open-source agreement to be more strict or relaxed as deemed necessary.
b. Your contributed code may be used for commercial purposes, including but not limited to its cloud business operations.`,
  apacheNotice: 'Apart from the specific conditions mentioned above, all other rights and restrictions follow the Apache License 2.0. Detailed information about the Apache License 2.0 can be found at http://www.apache.org/licenses/LICENSE-2.0.',
  patentNotice: 'The interactive design of this product is protected by appearance patent.',
  copyright: '© 2025 Jiangsu Qiantong Technology Co., Ltd'
}

const createStyles = () => {
  const rootStyles = getComputedStyle(document.documentElement)
  const primaryColor = rootStyles.getPropertyValue('--el-color-primary').trim() || '#2666fb'
  const primaryLightColor = rootStyles.getPropertyValue('--el-color-primary-light-9').trim() || '#e9effe'

  return {
    logo: [
      `color: ${primaryColor}`,
      'font-family: Menlo, Monaco, Consolas, monospace',
      'font-size: 13px',
      'font-weight: 700',
      'line-height: 1.2',
      `text-shadow: 0 1px 0 ${primaryLightColor}`
    ].join(';'),
    brand: [
      `background: ${primaryColor}`,
      'border-radius: 5px 0 0 5px',
      'color: #FFFFFF',
      'font-size: 12px',
      'font-weight: 700',
      'padding: 4px 10px'
    ].join(';'),
    version: [
      `background: ${primaryLightColor}`,
      'border-radius: 0 5px 5px 0',
      `color: ${primaryColor}`,
      'font-size: 12px',
      'font-weight: 700',
      'padding: 4px 10px'
    ].join(';'),
    licenseText: [
      'color: #475467',
      'font-family: -apple-system, BlinkMacSystemFont, Segoe UI, sans-serif',
      'font-size: 13px',
      'line-height: 1.55'
    ].join(';'),
    introText: [
      'color: #344054',
      'font-family: -apple-system, BlinkMacSystemFont, Segoe UI, sans-serif',
      'font-size: 13px',
      'font-weight: 500',
      'line-height: 1.6'
    ].join(';'),
    termTitle: [
      'color: #101828',
      'font-family: -apple-system, BlinkMacSystemFont, Segoe UI, sans-serif',
      'font-size: 13px',
      'font-weight: 700',
      'line-height: 1.6'
    ].join(';'),
    copyright: [
      'color: #667085',
      'font-size: 13px',
      'font-style: italic',
      'font-weight: 600'
    ].join(';'),
    websiteLabel: [
      'background: #067647',
      'border-radius: 4px 0 0 4px',
      'color: #FFFFFF',
      'font-weight: 700',
      'padding: 3px 8px'
    ].join(';'),
    websiteUrl: [
      'background: #ECFDF3',
      'border-radius: 0 4px 4px 0',
      'color: #067647',
      'font-weight: 700',
      'padding: 3px 8px'
    ].join(';')
  }
}

export function printConsoleBanner() {
  const styles = createStyles()
  const segments = [
    { text: `${logo}\n\n`, style: styles.logo },
    { text: 'VERSION', style: styles.brand },
    { text: `${packageInfo.version} `, style: styles.version },
    { text: `\n\n${licenseContent.intro}`, style: styles.introText },
    { text: `\n\n${licenseContent.commercialTitle}`, style: styles.termTitle },
    { text: `\n${licenseContent.commercialDetail}`, style: styles.licenseText },
    { text: `\n\n${licenseContent.contributorTitle}`, style: styles.termTitle },
    { text: `\n${licenseContent.contributorDetail}`, style: styles.licenseText },
    { text: `\n\n${licenseContent.apacheNotice}`, style: styles.licenseText },
    { text: `\n\n${licenseContent.patentNotice}`, style: styles.termTitle },
    { text: `\n${licenseContent.copyright}\n\n`, style: styles.copyright },
    { text: ' COMMUNITY ', style: styles.websiteLabel },
    { text: ' https://community.qdata.tech', style: styles.websiteUrl }
  ]

  console.info(
    segments.map(({ text }) => `%c${text}`).join(''),
    ...segments.map(({ style }) => style)
  )
}
