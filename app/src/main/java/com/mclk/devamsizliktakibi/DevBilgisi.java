package com.mclk.devamsizliktakibi;

public class DevBilgisi {
    static String  MesajVer(tblDers ders)
    {
        try {
            double yuzde = ders.getDevamsizlik()*100/ders.getDevSiniri();
            String sonucMesaji="Devamsızlık hakkınızı %"+(int)yuzde+" oranında tamamladınız.\n";
            if(yuzde>100)
                return sonucMesaji+"Devamsızlık sınırını geçmZişsiniz.\nDevamsızlıktan dolayı kalmamak için hoca ile iletişime geçmenizi öneririz.";
            else if(yuzde==100)
                return sonucMesaji+"Devamsızlık yapma hakkınız yok. Dikkatli olun.";
            else if(ders.getDevamsizlik()>=ders.getKritikSinir())
                return sonucMesaji+"Bu ders için belirlediğiniz kritik sınıra ulaştınız.\nDiğer haftalarda da derse gitmenizi öneririz.";
            else if(yuzde>=50)
                return sonucMesaji+"Artık derse gitmek için daha gayretli olmanız gerekiyor.";
            else
                return sonucMesaji+"Devamsızlık hakkınızı gayet verimli kullanıyorsunuz.";
        }
        catch(Exception ex)
        {
           return  ex.getMessage();
        }

    }
}
