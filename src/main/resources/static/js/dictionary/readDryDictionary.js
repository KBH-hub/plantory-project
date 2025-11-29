document.addEventListener('DOMContentLoaded', () => {
    const id = new URLSearchParams(location.search).get('cntntsNo');
    if (!id) return;

    const clean = (v) => (v == null ? '' : String(v).trim());
    const safe = (v) => (clean(v) || '-');

    const stripBrToSpace = (v) => {
        const s = clean(v);
        return s
            .replace(/<br\s*\/?>/gi, ' ')
            .replace(/<[^>]*>/g, '')
            .replace(/\s{2,}/g, ' ')
            .trim();
    };

    const $ = (i) => document.getElementById(i);

    const set = (id, val) => {
        const el = $(id);
        if (!el) return;
        el.textContent = safe(stripBrToSpace(val));
    };

    axios.get(`/api/dictionary/dry/${encodeURIComponent(id)}`, {headers: {Accept: 'application/json'}})
        .then(({data}) => {
            const item = data?.body?.item || {};

            const images = [
                clean(item.mainImgUrl1),
                clean(item.mainImgUrl2),
                clean(item.lightImgUrl1),
                clean(item.lightImgUrl2),
                clean(item.lightImgUrl3),
            ].filter(x => x !== '');

            const imgEl = $('plantImage');
            if (imgEl) {
                if (images.length) imgEl.src = images[0];

                const displayName = stripBrToSpace(item.cntntsSj || item.distbNm);
                if (displayName) imgEl.alt = displayName;

                if (images.length) {
                    const wrap = document.createElement('div');
                    wrap.className = 'mt-2 d-flex flex-wrap gap-2';

                    images.forEach(src => {
                        const a = document.createElement('a');
                        a.href = src;
                        a.target = '_blank';
                        a.rel = 'noopener';

                        const t = document.createElement('img');
                        t.src = src;
                        t.alt = displayName || '';
                        t.loading = 'lazy';
                        t.style.width = '64px';
                        t.style.height = '64px';
                        t.style.objectFit = 'cover';
                        t.className = 'border rounded';

                        t.addEventListener('click', (e) => {
                            e.preventDefault();
                            imgEl.src = src;
                        });

                        a.appendChild(t);
                        wrap.appendChild(a);
                    });

                    imgEl.parentElement && imgEl.parentElement.appendChild(wrap);
                }
            }

            set('plantName', item.cntntsSj || item.distbNm);
            set('scientificName', item.scnm);
            set('familyName', item.clNm);
            set('origin', item.orgplce);

            set('careLevel', item.manageLevelNm);
            set('pestInfo', item.dlthtsInfo);
            set('growthSpeed', item.grwtseVeNm);
            set('light', item.lighttInfo);
            set('leafColor', item.lfclChngeInfo);

            set('tempRange', item.grwhTpInfo || item.grwtInfo);
            set('winterTemp', item.pswntrTpInfo);
            set('humidity', item.hgtmMhmrInfo);

            // 물주기도 set로 통일
            set('watering', item.waterCycleInfo);

            set('growthType', item.stleSeNm);
            set('rootType', item.rdxStleNm);
            set('features', item.chartrInfo || item.lfclChngeInfo);

            set('flowerInfo', item.flwrInfo);
            set('fertilizerInfo', item.frtlzrInfo);
            set('propagation', item.prpgtInfo);
            set('tip', item.tipInfo || item.batchPlaceInfo);
        })
        .catch((e) => {
            console.error(e);
            [
                'plantName', 'scientificName', 'familyName', 'origin',
                'careLevel', 'pestInfo', 'growthSpeed', 'light', 'leafColor',
                'tempRange', 'winterTemp', 'humidity', 'watering',
                'growthType', 'rootType', 'features',
                'flowerInfo', 'fertilizerInfo', 'propagation', 'tip'
            ].forEach(id => {
                const el = document.getElementById(id);
                if (el) el.textContent = '-';
            });
        });

    document.querySelector('#back')?.addEventListener('click', () => {
        window.location.href = '/dryPlantDictionary';
    });
});
