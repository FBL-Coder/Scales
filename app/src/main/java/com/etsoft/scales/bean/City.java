package com.etsoft.scales.bean;

import java.util.List;

/**
 * Author：FBL  Time： 2018/8/15.
 */
public class City {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * code : 110000
         * name : 北京市
         * spell : beijingshi
         * abb : bjs
         * city : [{"id":2,"code":"110100","name":"市辖区","spell":"shixiaqu","abb":"sxq","area":[{"id":3,"code":"110101","name":"东城区","spell":"dongchengqu","abb":"dcq"},{"id":4,"code":"110102","name":"西城区","spell":"xichengqu","abb":"xcq"},{"id":5,"code":"110103","name":"崇文区","spell":"chongwenqu","abb":"cwq"},{"id":6,"code":"110104","name":"宣武区","spell":"xuanwuqu","abb":"xwq"},{"id":7,"code":"110105","name":"朝阳区","spell":"chaoyangqu","abb":"cyq"},{"id":8,"code":"110106","name":"丰台区","spell":"fengtaiqu","abb":"ftq"},{"id":9,"code":"110107","name":"石景山区","spell":"shijingshanqu","abb":"sjsq"},{"id":10,"code":"110108","name":"海淀区","spell":"haidianqu","abb":"hdq"},{"id":11,"code":"110109","name":"门头沟区","spell":"mentougouqu","abb":"mtgq"},{"id":12,"code":"110111","name":"房山区","spell":"fangshanqu","abb":"fsq"},{"id":13,"code":"110112","name":"通州区","spell":"tongzhouqu","abb":"tzq"},{"id":14,"code":"110113","name":"顺义区","spell":"shunyiqu","abb":"syq"},{"id":15,"code":"110114","name":"昌平区","spell":"changpingqu","abb":"cpq"},{"id":16,"code":"110115","name":"大兴区","spell":"daxingqu","abb":"dxq"},{"id":17,"code":"110116","name":"怀柔区","spell":"huairouqu","abb":"hrq"},{"id":18,"code":"110117","name":"平谷区","spell":"pingguqu","abb":"pgq"}]}]
         */

        private int id;
        private String code;
        private String name;
        private String spell;
        private String abb;
        private List<CityBean> city;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSpell() {
            return spell;
        }

        public void setSpell(String spell) {
            this.spell = spell;
        }

        public String getAbb() {
            return abb;
        }

        public void setAbb(String abb) {
            this.abb = abb;
        }

        public List<CityBean> getCity() {
            return city;
        }

        public void setCity(List<CityBean> city) {
            this.city = city;
        }

        public static class CityBean {
            /**
             * id : 2
             * code : 110100
             * name : 市辖区
             * spell : shixiaqu
             * abb : sxq
             * area : [{"id":3,"code":"110101","name":"东城区","spell":"dongchengqu","abb":"dcq"},{"id":4,"code":"110102","name":"西城区","spell":"xichengqu","abb":"xcq"},{"id":5,"code":"110103","name":"崇文区","spell":"chongwenqu","abb":"cwq"},{"id":6,"code":"110104","name":"宣武区","spell":"xuanwuqu","abb":"xwq"},{"id":7,"code":"110105","name":"朝阳区","spell":"chaoyangqu","abb":"cyq"},{"id":8,"code":"110106","name":"丰台区","spell":"fengtaiqu","abb":"ftq"},{"id":9,"code":"110107","name":"石景山区","spell":"shijingshanqu","abb":"sjsq"},{"id":10,"code":"110108","name":"海淀区","spell":"haidianqu","abb":"hdq"},{"id":11,"code":"110109","name":"门头沟区","spell":"mentougouqu","abb":"mtgq"},{"id":12,"code":"110111","name":"房山区","spell":"fangshanqu","abb":"fsq"},{"id":13,"code":"110112","name":"通州区","spell":"tongzhouqu","abb":"tzq"},{"id":14,"code":"110113","name":"顺义区","spell":"shunyiqu","abb":"syq"},{"id":15,"code":"110114","name":"昌平区","spell":"changpingqu","abb":"cpq"},{"id":16,"code":"110115","name":"大兴区","spell":"daxingqu","abb":"dxq"},{"id":17,"code":"110116","name":"怀柔区","spell":"huairouqu","abb":"hrq"},{"id":18,"code":"110117","name":"平谷区","spell":"pingguqu","abb":"pgq"}]
             */


            private int id;
            private String code;
            private String name;
            private String spell;
            private String abb;
            private List<AreaBean> area;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSpell() {
                return spell;
            }

            public void setSpell(String spell) {
                this.spell = spell;
            }

            public String getAbb() {
                return abb;
            }

            public void setAbb(String abb) {
                this.abb = abb;
            }

            public List<AreaBean> getArea() {
                return area;
            }

            public void setArea(List<AreaBean> area) {
                this.area = area;
            }

            public static class AreaBean {
                /**
                 * id : 3
                 * code : 110101
                 * name : 东城区
                 * spell : dongchengqu
                 * abb : dcq
                 */

                private int id;
                private String code;
                private String name;
                private String spell;
                private String abb;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getCode() {
                    return code;
                }

                public void setCode(String code) {
                    this.code = code;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getSpell() {
                    return spell;
                }

                public void setSpell(String spell) {
                    this.spell = spell;
                }

                public String getAbb() {
                    return abb;
                }

                public void setAbb(String abb) {
                    this.abb = abb;
                }
            }
        }
    }
}
