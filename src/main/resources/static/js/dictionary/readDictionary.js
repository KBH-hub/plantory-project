document.addEventListener('DOMContentLoaded', () => {
    const id = new URLSearchParams(location.search).get('cntntsNo');
    if (!id) return;

    axios.get(`/api/dictionary/garden/${encodeURIComponent(id)}`, {headers: {Accept: 'application/json'}})
        .then(({data}) => {
            // 합쳐진 응답 구조: { files: {...}, detail: {...} }
            const files = data?.files?.body?.items?.item || [];
            const imgEl = document.getElementById('plantImage');

            if (files.length && imgEl) {
                imgEl.src = files[0].rtnFileUrl || files[0].rtnThumbFileUrl || imgEl.src;
            }

            if (files.length && imgEl) {
                const wrap = document.createElement('div');
                wrap.className = 'mt-2 d-flex flex-wrap gap-2';

                files.forEach(f => {
                    const thumb = f.rtnThumbFileUrl || f.rtnFileUrl;
                    if (!thumb) return;
                    const a = document.createElement('a');
                    a.href = f.rtnFileUrl || thumb;
                    a.target = '_blank';
                    a.rel = 'noopener';

                    const t = document.createElement('img');
                    t.src = thumb;
                    t.alt = f.rtnImageDc || '';
                    t.style.width = '64px';
                    t.style.height = '64px';
                    t.style.objectFit = 'cover';
                    t.className = 'border rounded';

                    t.addEventListener('click', (e) => {
                        e.preventDefault();
                        imgEl.src = f.rtnFileUrl || thumb;
                    });

                    a.appendChild(t);
                    wrap.appendChild(a);
                });

                const imgParent = imgEl.parentElement;
                imgParent && imgParent.appendChild(wrap);
            }

            const item = data?.detail?.body?.item || {};
            const $ = (i) => document.getElementById(i);
            const text = (i, v) => {
                const el = $(i);
                if (el) el.textContent = (v && String(v).trim()) || '-';
            };

            text('plantName', item.distbNm);
            text('careLevel', item.managelevelCodeNm);
            text('pestInfo', item.dlthtsCodeNm);
            text('growthSpeed', item.grwtveCodeNm);
            text('smell', item.smellCodeNm);
            text('tempRange', item.grwhTpCodeNm);
            text('winterTemp', item.winterLwetTpCodeNm);
            text('humidity', item.hdCodeNm);
            {
                const wateringEl = document.getElementById('watering');
                if (wateringEl) {
                    const water = {
                        봄: item.watercycleSprngCodeNm,
                        여름: item.watercycleSummerCodeNm,
                        가을: item.watercycleAutumnCodeNm,
                        겨울: item.watercycleWinterCodeNm,
                    };
                    const order = ['봄', '여름', '가을', '겨울'];
                    wateringEl.innerHTML = order
                        .map(season => `<div>${season}: ${water[season] ? String(water[season]).trim() : '-'}</div>`)
                        .join('');
                }
            };
            text('familyName', item.fmlCodeNm);
            text('origin', item.orgplceInfo);
            text('category', item.clCodeNm || item.grwhstleCodeNm);
            text('flowerInfo', item.flclrCodeNm);
            text('leafColor', item.lefcolrCodeNm);
            text('growthType', item.grwhstleCodeNm || item.eclgyCodeNm);
            text('features', item.fncltyInfo);
            text('tip', item.adviseInfo);
        });
    document.querySelector('#back')?.addEventListener('click', () => {
        window.location.href = '/plantDictionary';
    });
});
