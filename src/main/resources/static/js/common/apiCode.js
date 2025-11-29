axios.defaults.baseURL = "/";
axios.defaults.headers.common["Accept"] = "application/json";

axios.interceptors.response.use(
    function (response) {
        const data = response.data;

        if (data && data.code && data.code !== "OK") {
            return Promise.reject({
                code: data.code,
                message: data.message,
                target: data.target
            });
        }

        return response;
    },

    function (error) {
        return Promise.reject(error);
    }
);
