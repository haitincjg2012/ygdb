<script>
  import B from '../../../assets/img/b.png'//求购
  import S from '../../../assets/img/s.png'//供求

  import daiban from '../../../assets/img/AgencyTips.png'//代办
  import zzHu from '../../../assets/img/AgencyTipsG.png'//种植户
  import keshang from '../../../assets/img/AgencyTipsK.png'//客商
  import lengku from '../../../assets/img/AgencyTipsL.png'//冷库
  import hezuoshang from  '../../../assets/img/hezuoshang.png'//合作商

  import QT from '../../../assets/img/copper@3x.png'//铜牌
  import BY from '../../../assets/img/silver@3x.png'//银牌
  import HJ from '../../../assets/img/gold@3x.png'//金牌
  import BJ from '../../../assets/img/Pt@3x.png'//铂金
  import ZS from '../../../assets/img/Diamonds.png'//钻石
  import DS from '../../../assets/img/Ancrown@3x.png'//大师

  export default{
      methods:{
          img(key){
                switch (key){
                case "供应":
                  return S;
                case "求购":
                  return B;
                }
          },
        userTypeNameSwitch(key){//用户身份转换
          switch (key) {
            case '代办':
              return daiban;
            case '种植户':
              return zzHu;
            case '冷库':
              return lengku;
            case '客商':
              return keshang;
              break;
            case '合作社':
              return hezuoshang;
              break;
            default:
              return '';
              break;
          }
        },
        userLevel(key){
          switch (key) {
            case '铜牌':
              return QT;
            case '银牌':
              return BY;
            case '金牌':
              return HJ;
            case '铂金':
              return BJ;
              break;
            case '钻石':
              return ZS;
              break;
            case '大师':
              return DS;
              break;
            default:
              return '';
              break;
          }
        }
      }
  }
</script>
