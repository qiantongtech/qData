<template>
    <div class="weather-wrap">
        <div class="weather-top">
            <div class="weather-location">
                <el-icon><Location /></el-icon>
                <span>{{ weather.location?.name || '-' }}</span>
            </div>
        </div>

        <div class="weather-main">
            <div class="weather-current">
                <img class="current-icon" :src="getWeatherIcon(weather.now?.text)" alt="" />
                <div class="temperature">{{ weather.now?.temperature || '--' }}</div>
                <div class="current-meta">
                    <div class="unit">℃</div>
                    <div class="text">{{ weather.now?.text || '-' }}</div>
                </div>
            </div>

            <div class="weather-metrics">
                <div class="metric-row">
                    <span class="metric-icon">
                        <svg
                            class="metric-svg"
                            viewBox="0 0 1024 1024"
                            xmlns="http://www.w3.org/2000/svg"
                        >
                            <path
                                d="M518.826667 85.504c-14.08-2.432-29.482667 3.413333-39.936 16-205.226667 246.186667-265.386667 372.906667-265.386667 538.666667a298.666667 298.666667 0 1 0 597.333333 0c0-60.842667-13.568-107.392-43.946666-155.989334-13.056-20.906667-78.08-106.453333-96-133.376C626.773333 284.672 588.8 211.2 552.234667 113.493333a44.245333 44.245333 0 0 0-33.365334-27.989333z m-107.946667 343.978667c10.410667-3.328 21.546667-4.053333 32 1.365333a42.581333 42.581333 0 0 1 18.645333 57.344c-27.989333 54.144-37.162667 106.666667-33.322666 150.613333a42.965333 42.965333 0 0 1-38.656 46.677334 42.965333 42.965333 0 0 1-46.677334-38.656c-5.205333-59.562667 6.997333-128.341333 42.666667-197.333334 5.418667-10.453333 14.933333-16.682667 25.344-20.053333z"
                                fill="#4f8efc"
                            />
                        </svg>
                    </span>
                    <span class="metric-label">湿度</span>
                    <span class="metric-value">{{ today?.humidity || '--' }}%</span>
                </div>
                <div class="metric-row">
                    <span class="metric-icon">
                        <svg
                            class="metric-svg"
                            viewBox="0 0 1024 1024"
                            xmlns="http://www.w3.org/2000/svg"
                        >
                            <path
                                d="M174.5 394.1h331.2c91 0 166-73.2 166.3-164.2 0.3-91-73.7-165.1-164.7-165.1-43.2 0-84 16.6-114.9 46.7-15.5 15.1-27.7 32.9-36.2 52.2-12.7 29.1 8.4 61.7 40.1 62.6 18.5 0.5 35.2-10.4 42.6-27.3 11.6-26.2 37.7-44.2 68.3-44.2 41.4 0 75.1 33.9 74.7 75.4-0.4 41.1-34.5 73.9-75.6 73.9H174.5c-24.9 0-45 20.1-45 45s20.2 45 45 45zM682 626.9c-0.6 0-0.8 0-427.3 6.3-7.3 0.1-14.2 0.2-20.5 0.3-24.8 0.4-44.7 20.8-44.3 45.6 0.3 24.9 20.8 44.8 45.7 44.4 6.3-0.1 13.2-0.2 20.5-0.3 124.9-1.9 415.3-6.2 426.2-6.3 42.8 0.2 77.2 36.5 74.2 80-2.8 39.8-35.9 70-75.7 69.3-30.2-0.5-55.9-18.5-67.2-44.5-7.3-16.8-24.1-27.5-42.5-27-31.7 0.9-52.8 33.5-40.1 62.6 8.4 19.4 20.6 37.1 36.2 52.2 30.9 30.1 71.8 46.7 115 46.7 91.8-0.1 166.8-77 164.5-168.8-2.3-88.9-75.3-160.5-164.7-160.5z"
                                fill="#4f8efc"
                            />
                            <path
                                d="M856.6 240.3c-29-11.5-60.5 10.1-60.5 41.4v1.3c0 18 10.9 34.4 27.6 41.1 27.5 11 47 38 47 69.4 0 41.2-33.5 74.7-74.7 74.7H112.6c-24.9 0-45 20.1-45 45s20.1 45 45 45h683.5c90.8 0 164.7-73.9 164.7-164.7-0.1-69.5-43.3-129-104.2-153.2z"
                                fill="#4f8efc"
                            />
                        </svg>
                    </span>
                    <span class="metric-label">风向</span>
                    <span class="metric-value">{{ todayWind }}</span>
                </div>
            </div>
        </div>

        <div class="weather-forecast">
            <div class="forecast-item" v-for="(item, index) in forecastList" :key="item.label">
                <img class="forecast-icon" :src="getWeatherIcon(item.text)" alt="" />
                <div class="forecast-day">
                    <div class="name">{{ item.label }}</div>
                    <div class="forecast-main">
                        <span class="desc">{{ item.text }}</span>
                        <span class="forecast-range">{{ item.high }}/{{ item.low }}℃</span>
                    </div>
                </div>
                <span v-if="index < forecastList.length - 1" class="forecast-divider"></span>
            </div>
        </div>
    </div>
</template>

<script setup name="Weather">
    import { computed, reactive } from 'vue';

    const fallbackWeather = {
        location: {
            name: '南京'
        },
        now: {
            text: '晴',
            temperature: '31'
        },
        daily: [
            {
                text_day: '晴',
                high: '31',
                low: '20',
                humidity: '84',
                wind_direction: '西北风',
                wind_scale: '2'
            },
            {
                text_day: '多云',
                high: '29',
                low: '19'
            },
            {
                text_day: '阴',
                high: '28',
                low: '18'
            }
        ]
    };

    const weather = reactive({
        location: fallbackWeather.location,
        now: fallbackWeather.now,
        daily: fallbackWeather.daily
    });

    const today = computed(() => weather.daily?.[0]);

    const todayWind = computed(() => {
        const windDirection = today.value?.wind_direction;
        const windScale = today.value?.wind_scale;
        if (!windDirection && !windScale) return '--';
        return `${windDirection || ''}${windScale ? windScale + '级' : ''}`;
    });

    const forecastList = computed(() => {
        const labels = ['今天', '明天', '后天'];
        return labels.map((label, index) => {
            const item = weather.daily?.[index] || {};
            return {
                label,
                text: item.text_day || item.text_night || '-',
                high: item.high || '--',
                low: item.low || '--'
            };
        });
    });

    const iconNameMap = {
        大暴雨: 'img-extremely-heavy-rainstorm',
        小雨: 'img-rain',
        中雨: 'img-rain',
        大雨: 'img-rain',
        阴: 'img-cloudy',
        小雪: 'img-snow',
        中雪: 'img-snow',
        大雪: 'img-snow',
        大风: 'img-wind',
        飓风: 'img-wind',
        热带风暴: 'img-wind',
        龙卷风: 'img-wind'
    };

    const knownIconNames = [
        'img-sunny',
        'img-cloudy',
        'img-cloudy-to-sunny',
        'img-rainstorm',
        'img-thundershower',
        'img-thunderstorms-hail',
        'img-extremely-heavy-rainstorm',
        'img-snow',
        'img-snow-shower',
        'img-blizzard',
        'img-sleet',
        'img-mist',
        'img-fog',
        'img-typhoon',
        'img-rain',
        'img-strong-wind',
        'img-heavy-shower',
        'img-wind'
    ];

    function getWeatherIcon(name) {
        const iconName = iconNameMap[name] || (knownIconNames.includes(name) ? name : 'img-sunny');
        return new URL(`../../assets/images/system/weather/${iconName}.png`, import.meta.url).href;
    }

    async function getData() {
        const baseUrl = 'https://api.seniverse.com/v3/weather';
        const baseParams = 'key=SjyiLD_odjCGOsHoF&location=南京&language=zh-Hans&unit=c';

        try {
            const [nowRes, dailyRes] = await Promise.all([
                fetch(`${baseUrl}/now.json?${baseParams}`),
                fetch(`${baseUrl}/daily.json?${baseParams}&start=0&days=3`)
            ]);
            const [nowData, dailyData] = await Promise.all([nowRes.json(), dailyRes.json()]);
            const nowResult = nowData?.results?.[0];
            const dailyResult = dailyData?.results?.[0];

            weather.location =
                dailyResult?.location || nowResult?.location || fallbackWeather.location;
            weather.now = nowResult?.now || fallbackWeather.now;
            weather.daily = dailyResult?.daily?.length ? dailyResult.daily : fallbackWeather.daily;
        } catch (error) {
            weather.location = fallbackWeather.location;
            weather.now = fallbackWeather.now;
            weather.daily = fallbackWeather.daily;
        }
    }

    getData();
</script>

<style scoped lang="scss">
    .weather-wrap {
        width: 100%;
        height: 150px;
        box-sizing: border-box;
        padding: 12px 15px 9px;
        background: url('@/assets/images/system/weather/bg-tq.jpg') no-repeat center center;
        background-size: 100% 100%;
        color: #1f2f4a;
        overflow: hidden;
        text-align: left;
        container-type: inline-size;
    }

    .weather-top,
    .weather-location,
    .weather-main,
    .weather-current,
    .weather-metrics,
    .metric-row,
    .weather-forecast,
    .forecast-item {
        display: flex;
        align-items: center;
    }

    .weather-top {
        height: 21px;
    }

    .weather-location {
        gap: 5px;
        font-size: 14px;
        line-height: 20px;
        font-weight: 700;
        color: #1d2b45;

        .el-icon {
            font-size: 18px;
            color: #1677ff;
        }
    }

    .weather-main {
        display: grid;
        grid-template-columns: minmax(132px, 1fr) 152px;
        column-gap: 10px;
        height: 60px;
        align-items: center;
    }

    .weather-current {
        min-width: 0;
        height: 60px;
    }

    .current-icon {
        width: 56px;
        height: 56px;
        object-fit: contain;
        margin: 0 8px 0 0;
    }

    .temperature {
        font-size: 50px;
        line-height: 55px;
        font-weight: 300;
        color: #1268e9;
        letter-spacing: 0;
        font-family: Arial, Helvetica, sans-serif;
    }

    .current-meta {
        align-self: stretch;
        display: flex;
        flex-direction: column;
        justify-content: center;
        margin-left: 4px;

        .unit {
            font-size: 16px;
            line-height: 19px;
            color: #1268e9;
            font-weight: 600;
        }

        .text {
            margin-top: 5px;
            font-size: 12px;
            line-height: 16px;
            color: #596477;
        }
    }

    .weather-metrics {
        position: relative;
        box-sizing: border-box;
        flex-direction: column;
        align-items: stretch;
        justify-content: center;
        justify-self: start;
        gap: 8px;
        width: 132px;
        min-width: 0;
    }

    .metric-row {
        height: 23px;
        font-size: 12px;
        line-height: 23px;
        color: #354259;
        display: grid;
        grid-template-columns: 23px 34px minmax(0, 1fr);
        column-gap: 11px;
        align-items: center;
    }

    .metric-icon {
        position: relative;
        width: 23px;
        height: 23px;
        flex: 0 0 23px;
        border-radius: 50%;
        background: rgba(255, 255, 255, 0.76);
        box-shadow: 0 8px 18px rgba(62, 133, 232, 0.12);
        display: flex;
        align-items: center;
        justify-content: center;

        &::after {
            content: '';
            position: absolute;
            right: -6px;
            top: 5px;
            width: 1px;
            height: 13px;
            background: rgba(112, 152, 203, 0.35);
        }
    }

    .metric-svg {
        width: 13px;
        height: 13px;
    }

    .metric-label {
        color: #39455b;
    }

    .metric-value {
        color: #1f2f4a;
        font-weight: 500;
        white-space: nowrap;
        text-align: left;
    }

    .weather-forecast {
        display: grid;
        grid-template-columns: repeat(3, minmax(0, 1fr));
        align-items: center;
        min-width: 0;
        height: 43px;
        margin-top: 5px;
        padding: 0 6px;
        overflow: hidden;
        border-radius: 2px;
        background: linear-gradient(
            180deg,
            rgba(255, 255, 255, 0.34) 0%,
            rgba(226, 241, 255, 0.46) 100%
        );
        box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.28);
        border-radius: 25px;
    }

    .forecast-item {
        position: relative;
        min-width: 0;
        height: 43px;
        padding: 0 4px;
        display: grid;
        grid-template-columns: 24px minmax(0, 74px);
        column-gap: 5px;
        align-items: center;
        justify-content: center;
        overflow: hidden;

        &:first-child {
            padding-left: 2px;
        }

        &:last-child {
            padding-right: 2px;
        }
    }

    .forecast-icon {
        width: 24px;
        height: 24px;
        object-fit: contain;
        justify-self: end;
    }

    .forecast-day {
        min-width: 0;
        width: 100%;
        text-align: left;

        .forecast-main {
            display: grid;
            grid-template-columns: 28px max-content;
            align-items: center;
            column-gap: 4px;
            white-space: nowrap;
        }

        .name {
            font-size: 12px;
            line-height: 17px;
            color: #293954;
            font-weight: 500;
        }

        .desc {
            display: block;
            width: 28px;
            overflow: hidden;
            text-overflow: ellipsis;
            font-size: 12px;
            line-height: 16px;
            color: #7d8798;
        }
    }

    .forecast-range {
        color: #25354f;
        font-size: 12px;
        line-height: 16px;
        white-space: nowrap;
    }

    .forecast-divider {
        position: absolute;
        right: 0;
        top: 8px;
        width: 1px;
        height: 27px;
        background: rgba(120, 154, 199, 0.24);
    }

    @container (max-width: 360px) {
        .weather-forecast {
            padding: 0 4px;
        }

        .forecast-item {
            grid-template-columns: 22px minmax(0, 66px);
            column-gap: 4px;
            padding: 0 2px;
        }

        .forecast-icon {
            width: 22px;
            height: 22px;
        }

        .forecast-day {
            .forecast-main {
                grid-template-columns: 24px max-content;
                column-gap: 3px;
            }

            .name,
            .desc {
                font-size: 11px;
            }

            .desc {
                width: 24px;
            }
        }

        .forecast-range {
            font-size: 11px;
        }
    }

    @container (max-width: 300px) {
        .weather-forecast {
            padding: 0 3px;
        }

        .forecast-item {
            grid-template-columns: 20px minmax(0, 1fr);
            column-gap: 3px;
            padding: 0 1px;
        }

        .forecast-icon {
            width: 20px;
            height: 20px;
        }

        .forecast-day {
            .forecast-main {
                grid-template-columns: minmax(0, max-content);
                column-gap: 0;
            }

            .desc {
                display: none;
            }
        }
    }

    @container (min-width: 380px) {
        .weather-main {
            grid-template-columns: minmax(152px, 1fr) 156px;
            column-gap: 11px;
        }

        .current-icon {
            width: 58px;
            height: 58px;
            margin-right: 9px;
        }

        .temperature {
            font-size: 52px;
            line-height: 57px;
        }

        .weather-metrics {
            width: 136px;
        }

        .metric-row {
            grid-template-columns: 24px 34px minmax(0, 1fr);
            column-gap: 12px;
        }

        .metric-icon {
            width: 24px;
            height: 24px;

            &::after {
                right: -7px;
                top: 5px;
                height: 14px;
            }
        }

        .forecast-item {
            grid-template-columns: 26px minmax(0, max-content);
            column-gap: 6px;
            padding: 0 5px;
        }

        .forecast-icon {
            width: 26px;
            height: 26px;
        }

        .forecast-day {
            .forecast-main {
                grid-template-columns: 30px max-content;
                column-gap: 6px;
            }

            .desc {
                width: 30px;
            }
        }
    }
</style>
