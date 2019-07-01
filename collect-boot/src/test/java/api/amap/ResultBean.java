package api.amap;

import java.util.List;

public class ResultBean {

    /**
     * ret : 200
     * msg : success
     * data : [{"pinyin":"DongHuaMen","lng":"116.406708","level":3,"parent_id":3,"area_code":"110101001000","name":"东华门街道办事处","merger_name":"北京,东城,东华门","city_code":"010","short_name":"东华门","id":4,"zip_code":"100006","lat":"39.914219"},{"pinyin":"JingShan","lng":"116.404068","level":3,"parent_id":3,"area_code":"110101002000","name":"景山街道办事处","merger_name":"北京,东城,景山","city_code":"010","short_name":"景山","id":17,"zip_code":"100009","lat":"39.923553"},{"pinyin":"JiaoDaoKou","lng":"116.408543","level":3,"parent_id":3,"area_code":"110101003000","name":"交道口街道办事处","merger_name":"北京,东城,交道口","city_code":"010","short_name":"交道口","id":26,"zip_code":"100007","lat":"39.940845"},{"pinyin":"AnDingMen","lng":"116.407806","level":3,"parent_id":3,"area_code":"110101004000","name":"安定门街道办事处","merger_name":"北京,东城,安定门","city_code":"010","short_name":"安定门","id":34,"zip_code":"100007","lat":"39.949892"},{"pinyin":"BeiXinQiao","lng":"116.416884","level":3,"parent_id":3,"area_code":"110101005000","name":"北新桥街道办事处","merger_name":"北京,东城,北新桥","city_code":"010","short_name":"北新桥","id":44,"zip_code":"100007","lat":"39.940782"},{"pinyin":"DongSi","lng":"116.416357","level":3,"parent_id":3,"area_code":"110101006000","name":"东四街道办事处","merger_name":"北京,东城,东四","city_code":"010","short_name":"东四","id":57,"zip_code":"100005","lat":"39.928353"},{"pinyin":"ZhaoYangMen","lng":"116.433666","level":3,"parent_id":3,"area_code":"110101007000","name":"朝阳门街道办事处","merger_name":"北京,东城,朝阳门","city_code":"010","short_name":"朝阳门","id":65,"zip_code":"100005","lat":"39.923823"},{"pinyin":"JianGuoMen","lng":"116.434806","level":3,"parent_id":3,"area_code":"110101008000","name":"建国门街道办事处","merger_name":"北京,东城,建国门","city_code":"010","short_name":"建国门","id":75,"zip_code":"100005","lat":"39.911970"},{"pinyin":"DongZhiMen","lng":"116.426319","level":3,"parent_id":3,"area_code":"110101009000","name":"东直门街道办事处","merger_name":"北京,东城,东直门","city_code":"010","short_name":"东直门","id":83,"zip_code":"100007","lat":"39.941823"},{"pinyin":"HePingLi","lng":"116.421257","level":3,"parent_id":3,"area_code":"110101010000","name":"和平里街道办事处","merger_name":"北京,东城,和平里","city_code":"010","short_name":"和平里","id":94,"zip_code":"100013","lat":"39.959267"},{"pinyin":"QianMen","lng":"116.416357","level":3,"parent_id":3,"area_code":"110101011000","name":"前门街道办事处","merger_name":"北京,东城,前门","city_code":"010","short_name":"前门","id":115,"zip_code":"100006","lat":"39.928353"},{"pinyin":"ChongWenMenWai","lng":"116.416780","level":3,"parent_id":3,"area_code":"110101012000","name":"崇文门外街道办事处","merger_name":"北京,东城,崇文门外","city_code":"010","short_name":"崇文门外","id":120,"zip_code":"100062","lat":"39.898027"},{"pinyin":"DongHuaShi","lng":"116.425678","level":3,"parent_id":3,"area_code":"110101013000","name":"东花市街道办事处","merger_name":"北京,东城,东花市","city_code":"010","short_name":"东花市","id":133,"zip_code":"100062","lat":"39.897101"},{"pinyin":"LongTan","lng":"116.434499","level":3,"parent_id":3,"area_code":"110101014000","name":"龙潭街道办事处","merger_name":"北京,东城,龙潭","city_code":"010","short_name":"龙潭","id":142,"zip_code":"100061","lat":"39.881734"},{"pinyin":"TiYuGuanLu","lng":"116.425411","level":3,"parent_id":3,"area_code":"110101015000","name":"体育馆路街道办事处","merger_name":"北京,东城,体育馆路","city_code":"010","short_name":"体育馆路","id":154,"zip_code":"100061","lat":"39.883966"},{"pinyin":"TianTan","lng":"116.414936","level":3,"parent_id":3,"area_code":"110101016000","name":"天坛街道办事处","merger_name":"北京,东城,天坛","city_code":"010","short_name":"天坛","id":165,"zip_code":"100050","lat":"39.867550"},{"pinyin":"YongDingMenWai","lng":"116.398559","level":3,"parent_id":3,"area_code":"110101017000","name":"永定门外街道办事处","merger_name":"北京,东城,永定门外","city_code":"010","short_name":"永定门外","id":182,"zip_code":"100075","lat":"39.868329"},{"pinyin":"XiChangAnJie","lng":"116.385514","level":3,"parent_id":203,"area_code":"110102001000","name":"西长安街街道办事处","merger_name":"北京,西城,西长安街","city_code":"010","short_name":"西长安街","id":204,"zip_code":"100017","lat":"39.907342"},{"pinyin":"XinJieKou","lng":"116.372703","level":3,"parent_id":203,"area_code":"110102003000","name":"新街口街道办事处","merger_name":"北京,西城,新街口","city_code":"010","short_name":"新街口","id":218,"zip_code":"100035","lat":"39.940623"},{"pinyin":"YueTan","lng":"116.365868","level":3,"parent_id":203,"area_code":"110102007000","name":"月坛街道办事处","merger_name":"北京,西城,月坛","city_code":"010","short_name":"月坛","id":240,"zip_code":"100045","lat":"39.912289"},{"pinyin":"ZhanLanLu","lng":"116.344635","level":3,"parent_id":203,"area_code":"110102009000","name":"展览路街道办事处","merger_name":"北京,西城,展览路","city_code":"010","short_name":"展览路","id":268,"zip_code":"100044","lat":"39.930912"},{"pinyin":"DeSheng","lng":"116.383172","level":3,"parent_id":203,"area_code":"110102010000","name":"德胜街道办事处","merger_name":"北京,西城,德胜","city_code":"010","short_name":"德胜","id":291,"zip_code":"100009","lat":"39.962501"},{"pinyin":"JinRongJie","lng":"116.357325","level":3,"parent_id":203,"area_code":"110102011000","name":"金融街街道办事处","merger_name":"北京,西城,金融街","city_code":"010","short_name":"金融街","id":316,"zip_code":"100032","lat":"39.910142"},{"pinyin":"ShiChaHai","lng":"116.385307","level":3,"parent_id":203,"area_code":"110102012000","name":"什刹海街道办事处","merger_name":"北京,西城,什刹海","city_code":"010","short_name":"什刹海","id":336,"zip_code":"100033","lat":"39.941853"},{"pinyin":"DaZhaLan","lng":"116.391136","level":3,"parent_id":203,"area_code":"110102013000","name":"大栅栏街道办事处","merger_name":"北京,西城,大栅栏","city_code":"010","short_name":"大栅栏","id":362,"zip_code":"100051","lat":"39.895092"},{"pinyin":"TianQiao","lng":"116.398526","level":3,"parent_id":203,"area_code":"110102014000","name":"天桥街道办事处","merger_name":"北京,西城,天桥","city_code":"010","short_name":"天桥","id":372,"zip_code":"102399","lat":"39.886517"},{"pinyin":"ChunShu","lng":"116.365868","level":3,"parent_id":203,"area_code":"110102015000","name":"椿树街道办事处","merger_name":"北京,西城,椿树","city_code":"010","short_name":"椿树","id":381,"zip_code":"100032","lat":"39.912289"},{"pinyin":"TaoRanTing","lng":"116.386582","level":3,"parent_id":203,"area_code":"110102016000","name":"陶然亭街道办事处","merger_name":"北京,西城,陶然亭","city_code":"010","short_name":"陶然亭","id":389,"zip_code":"100050","lat":"39.878592"},{"pinyin":"GuangAnMenNei","lng":"116.358239","level":3,"parent_id":203,"area_code":"110102017000","name":"广安门内街道办事处","merger_name":"北京,西城,广安门内","city_code":"010","short_name":"广安门内","id":400,"zip_code":"100053","lat":"39.889418"},{"pinyin":"NiuJie","lng":"116.363676","level":3,"parent_id":203,"area_code":"110102018000","name":"牛街街道办事处","merger_name":"北京,西城,牛街","city_code":"010","short_name":"牛街","id":419,"zip_code":"100053","lat":"39.885559"},{"pinyin":"BaiZhiFang","lng":"116.361938","level":3,"parent_id":203,"area_code":"110102019000","name":"白纸坊街道办事处","merger_name":"北京,西城,白纸坊","city_code":"010","short_name":"白纸坊","id":430,"zip_code":"100053","lat":"39.879288"},{"pinyin":"GuangAnMenWai","lng":"116.340938","level":3,"parent_id":203,"area_code":"110102020000","name":"广安门外街道办事处","merger_name":"北京,西城,广安门外","city_code":"010","short_name":"广安门外","id":449,"zip_code":"100055","lat":"39.892158"},{"pinyin":"JianWai","lng":"116.451606","level":3,"parent_id":480,"area_code":"110105001000","name":"建外街道办事处","merger_name":"北京,朝阳,建外","city_code":"010","short_name":"建外","id":481,"zip_code":"100022","lat":"39.908226"},{"pinyin":"ChaoWai","lng":"116.443877","level":3,"parent_id":480,"area_code":"110105002000","name":"朝外街道办事处","merger_name":"北京,朝阳,朝外","city_code":"010","short_name":"朝外","id":490,"zip_code":"100020","lat":"39.924659"},{"pinyin":"HuJiaLou","lng":"116.462410","level":3,"parent_id":480,"area_code":"110105003000","name":"呼家楼街道办事处","merger_name":"北京,朝阳,呼家楼","city_code":"010","short_name":"呼家楼","id":498,"zip_code":"100026","lat":"39.920246"},{"pinyin":"SanLiTun","lng":"116.455294","level":3,"parent_id":480,"area_code":"110105004000","name":"三里屯街道办事处","merger_name":"北京,朝阳,三里屯","city_code":"010","short_name":"三里屯","id":509,"zip_code":"100600","lat":"39.937492"},{"pinyin":"ZuoJiaZhuang","lng":"116.446705","level":3,"parent_id":480,"area_code":"110105005000","name":"左家庄街道办事处","merger_name":"北京,朝阳,左家庄","city_code":"010","short_name":"左家庄","id":517,"zip_code":"100028","lat":"39.953363"},{"pinyin":"XiangHeYuan","lng":"116.442075","level":3,"parent_id":480,"area_code":"110105006000","name":"香河园街道办事处","merger_name":"北京,朝阳,香河园","city_code":"010","short_name":"香河园","id":527,"zip_code":"100028","lat":"39.949468"},{"pinyin":"HePingJie","lng":"116.420158","level":3,"parent_id":480,"area_code":"110105007000","name":"和平街街道办事处","merger_name":"北京,朝阳,和平街","city_code":"010","short_name":"和平街","id":537,"zip_code":"100013","lat":"39.964705"},{"pinyin":"AnZhen","lng":"116.406082","level":3,"parent_id":480,"area_code":"110105008000","name":"安贞街道办事处","merger_name":"北京,朝阳,安贞","city_code":"010","short_name":"安贞","id":547,"zip_code":"100029","lat":"39.966709"},{"pinyin":"YaYunCun","lng":"116.408027","level":3,"parent_id":480,"area_code":"110105009000","name":"亚运村街道办事处","merger_name":"北京,朝阳,亚运村","city_code":"010","short_name":"亚运村","id":554,"zip_code":"100101","lat":"39.991761"},{"pinyin":"XiaoGuan","lng":"116.413765","level":3,"parent_id":480,"area_code":"110105010000","name":"小关街道办事处","merger_name":"北京,朝阳,小关","city_code":"010","short_name":"小关","id":565,"zip_code":"100029","lat":"39.980392"},{"pinyin":"JiuXianQiao","lng":"116.494721","level":3,"parent_id":480,"area_code":"110105011000","name":"酒仙桥街道办事处","merger_name":"北京,朝阳,酒仙桥","city_code":"010","short_name":"酒仙桥","id":573,"zip_code":"100015","lat":"39.973688"},{"pinyin":"MaiZiDian","lng":"116.473457","level":3,"parent_id":480,"area_code":"110105012000","name":"麦子店街道办事处","merger_name":"北京,朝阳,麦子店","city_code":"010","short_name":"麦子店","id":583,"zip_code":"100125","lat":"39.946871"},{"pinyin":"TuanJieHu","lng":"116.466998","level":3,"parent_id":480,"area_code":"110105013000","name":"团结湖街道办事处","merger_name":"北京,朝阳,团结湖","city_code":"010","short_name":"团结湖","id":589,"zip_code":"100026","lat":"39.927661"},{"pinyin":"LiuLiTun","lng":"116.486577","level":3,"parent_id":480,"area_code":"110105014000","name":"六里屯街道办事处","merger_name":"北京,朝阳,六里屯","city_code":"010","short_name":"六里屯","id":596,"zip_code":"100026","lat":"39.928376"},{"pinyin":"BaLiZhuang","lng":"116.480622","level":3,"parent_id":480,"area_code":"110105015000","name":"八里庄街道办事处","merger_name":"北京,朝阳,八里庄","city_code":"010","short_name":"八里庄","id":608,"zip_code":"100025","lat":"39.914804"},{"pinyin":"ShuangJing","lng":"116.469580","level":3,"parent_id":480,"area_code":"110105016000","name":"双井街道办事处","merger_name":"北京,朝阳,双井","city_code":"010","short_name":"双井","id":623,"zip_code":"100022","lat":"39.899119"},{"pinyin":"JinSong","lng":"116.455111","level":3,"parent_id":480,"area_code":"110105017000","name":"劲松街道办事处","merger_name":"北京,朝阳,劲松","city_code":"010","short_name":"劲松","id":636,"zip_code":"100021","lat":"39.884433"},{"pinyin":"PanJiaYuan","lng":"116.454982","level":3,"parent_id":480,"area_code":"110105018000","name":"潘家园街道办事处","merger_name":"北京,朝阳,潘家园","city_code":"010","short_name":"潘家园","id":650,"zip_code":"100021","lat":"39.878934"}]
     * log_id : 0F57CAB6-4975-4B85-8361-33E1454FC53F
     */

    private int ret;
    private String msg;
    private String log_id;
    private List<DataBean> data;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getLog_id() {
        return log_id;
    }

    public void setLog_id(String log_id) {
        this.log_id = log_id;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * pinyin : DongHuaMen
         * lng : 116.406708
         * level : 3
         * parent_id : 3
         * area_code : 110101001000
         * name : 东华门街道办事处
         * merger_name : 北京,东城,东华门
         * city_code : 010
         * short_name : 东华门
         * id : 4
         * zip_code : 100006
         * lat : 39.914219
         */

        private String pinyin;
        private String lng;
        private int level;
        private int parent_id;
        private String area_code;
        private String name;
        private String merger_name;
        private String city_code;
        private String short_name;
        private int id;
        private String zip_code;
        private String lat;

        public String getPinyin() {
            return pinyin;
        }

        public void setPinyin(String pinyin) {
            this.pinyin = pinyin;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getParent_id() {
            return parent_id;
        }

        public void setParent_id(int parent_id) {
            this.parent_id = parent_id;
        }

        public String getArea_code() {
            return area_code;
        }

        public void setArea_code(String area_code) {
            this.area_code = area_code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMerger_name() {
            return merger_name;
        }

        public void setMerger_name(String merger_name) {
            this.merger_name = merger_name;
        }

        public String getCity_code() {
            return city_code;
        }

        public void setCity_code(String city_code) {
            this.city_code = city_code;
        }

        public String getShort_name() {
            return short_name;
        }

        public void setShort_name(String short_name) {
            this.short_name = short_name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getZip_code() {
            return zip_code;
        }

        public void setZip_code(String zip_code) {
            this.zip_code = zip_code;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }
    }
}
