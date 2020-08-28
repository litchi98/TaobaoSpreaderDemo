package com.litchi.taobaodemo.model.bean;

public class CouponCode {

    /**
     * success : true
     * code : 10000
     * message : 淘宝口令构建成功!
     * data : {"tbk_tpwd_create_response":{"data":{"model":"￥xhQoYC66sMX￥"},"request_id":"64jzpdn6m026"}}
     */

    private boolean success;
    private int code;
    private String message;
    private DataBeanX data;

    @Override
    public String toString() {
        return "CouponCode{" +
                "success=" + success +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {
        public DataBeanX(TbkTpwdCreateResponseBean tbk_tpwd_create_response) {
            this.tbk_tpwd_create_response = tbk_tpwd_create_response;
        }

        /**
         * tbk_tpwd_create_response : {"data":{"model":"￥xhQoYC66sMX￥"},"request_id":"64jzpdn6m026"}
         */



        private TbkTpwdCreateResponseBean tbk_tpwd_create_response;

        public TbkTpwdCreateResponseBean getTbk_tpwd_create_response() {
            if (tbk_tpwd_create_response == null) {
                tbk_tpwd_create_response = new TbkTpwdCreateResponseBean(null, null);
            }
            return tbk_tpwd_create_response;
        }

        public void setTbk_tpwd_create_response(TbkTpwdCreateResponseBean tbk_tpwd_create_response) {
            this.tbk_tpwd_create_response = tbk_tpwd_create_response;
        }

        public static class TbkTpwdCreateResponseBean {
            /**
             * data : {"model":"￥xhQoYC66sMX￥"}
             * request_id : 64jzpdn6m026
             */

            private DataBean data;
            private String request_id;

            public TbkTpwdCreateResponseBean(DataBean data, String request_id) {
                this.data = data;
                this.request_id = request_id;
            }

            @Override
            public String toString() {
                return "TbkTpwdCreateResponseBean{" +
                        "data=" + data +
                        ", request_id='" + request_id + '\'' +
                        '}';
            }

            public DataBean getData() {
                if (data == null) {
                    data = new DataBean("");
                }
                return data;
            }

            public void setData(DataBean data) {
                this.data = data;
            }

            public String getRequest_id() {
                return request_id;
            }

            public void setRequest_id(String request_id) {
                this.request_id = request_id;
            }

            public static class DataBean {
                /**
                 * model : ￥xhQoYC66sMX￥
                 */

                private String model;

                public DataBean(String model) {
                    this.model = model;
                }

                public String getModel() {
                    return model;
                }

                public void setModel(String model) {
                    this.model = model;
                }

                @Override
                public String toString() {
                    return "DataBean{" +
                            "model='" + model + '\'' +
                            '}';
                }
            }
        }

        @Override
        public String toString() {
            return "DataBeanX{" +
                    "tbk_tpwd_create_response=" + tbk_tpwd_create_response +
                    '}';
        }
    }
}
