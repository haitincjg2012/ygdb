<script>
  import B from '../assets/img/b.png'//求购
  import S from '../assets/img/s.png'//供求

  import daiban from '../assets/img/AgencyTips.png'//代办
  import zzHu from '../assets/img/AgencyTipsG.png'//种植户
  import keshang from '../assets/img/AgencyTipsK.png'//客商
  import lengku from '../assets/img/AgencyTipsL.png'//冷库
  import hezuoshang from  '../assets/img/hezuoshang.png'//合作商

  import QT from '../assets/img/t.png'//铜牌
  import BY from '../assets/img/y.png'//银牌
  import HJ from '../assets/img/j.png'//金牌
  import BJ from '../assets/img/bj-1.png'//铂金
  import ZS from '../assets/img/zs.png'//钻石
//  import DS from '../assets/img/ds.png'//大师

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
            case 'QT':
              return QT;
            case 'BY':
              return BY;
            case 'HJ':
              return HJ;
            case 'BJ':
              return BJ;
              break;
            case 'ZS':
              return ZS;
              break;
            case 'DS':
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
