/*
************************************************************************
*******************  CANADIAN ASTRONOMY DATA CENTRE  *******************
**************  CENTRE CANADIEN DE DONNÉES ASTRONOMIQUES  **************
*
*  (c) 2009.                            (c) 2009.
*  Government of Canada                 Gouvernement du Canada
*  National Research Council            Conseil national de recherches
*  Ottawa, Canada, K1A 0R6              Ottawa, Canada, K1A 0R6
*  All rights reserved                  Tous droits réservés
*                                       
*  NRC disclaims any warranties,        Le CNRC dénie toute garantie
*  expressed, implied, or               énoncée, implicite ou légale,
*  statutory, of any kind with          de quelque nature que ce
*  respect to the software,             soit, concernant le logiciel,
*  including without limitation         y compris sans restriction
*  any warranty of merchantability      toute garantie de valeur
*  or fitness for a particular          marchande ou de pertinence
*  purpose. NRC shall not be            pour un usage particulier.
*  liable in any event for any          Le CNRC ne pourra en aucun cas
*  damages, whether direct or           être tenu responsable de tout
*  indirect, special or general,        dommage, direct ou indirect,
*  consequential or incidental,         particulier ou général,
*  arising from the use of the          accessoire ou fortuit, résultant
*  software.  Neither the name          de l'utilisation du logiciel. Ni
*  of the National Research             le nom du Conseil National de
*  Council of Canada nor the            Recherches du Canada ni les noms
*  names of its contributors may        de ses  participants ne peuvent
*  be used to endorse or promote        être utilisés pour approuver ou
*  products derived from this           promouvoir les produits dérivés
*  software without specific prior      de ce logiciel sans autorisation
*  written permission.                  préalable et particulière
*                                       par écrit.
*                                       
*  This file is part of the             Ce fichier fait partie du projet
*  OpenCADC project.                    OpenCADC.
*                                       
*  OpenCADC is free software:           OpenCADC est un logiciel libre ;
*  you can redistribute it and/or       vous pouvez le redistribuer ou le
*  modify it under the terms of         modifier suivant les termes de
*  the GNU Affero General Public        la “GNU Affero General Public
*  License as published by the          License” telle que publiée
*  Free Software Foundation,            par la Free Software Foundation
*  either version 3 of the              : soit la version 3 de cette
*  License, or (at your option)         licence, soit (à votre gré)
*  any later version.                   toute version ultérieure.
*                                       
*  OpenCADC is distributed in the       OpenCADC est distribué
*  hope that it will be useful,         dans l’espoir qu’il vous
*  but WITHOUT ANY WARRANTY;            sera utile, mais SANS AUCUNE
*  without even the implied             GARANTIE : sans même la garantie
*  warranty of MERCHANTABILITY          implicite de COMMERCIALISABILITÉ
*  or FITNESS FOR A PARTICULAR          ni d’ADÉQUATION À UN OBJECTIF
*  PURPOSE.  See the GNU Affero         PARTICULIER. Consultez la Licence
*  General Public License for           Générale Publique GNU Affero
*  more details.                        pour plus de détails.
*                                       
*  You should have received             Vous devriez avoir reçu une
*  a copy of the GNU Affero             copie de la Licence Générale
*  General Public License along         Publique GNU Affero avec
*  with OpenCADC.  If not, see          OpenCADC ; si ce n’est
*  <http://www.gnu.org/licenses/>.      pas le cas, consultez :
*                                       <http://www.gnu.org/licenses/>.
*
*  $Revision: 4 $
*
************************************************************************
*/


package test;

import ca.nrc.cadc.dlm.client.event.ConsoleEventLogger;
import ca.nrc.cadc.dlm.client.Download;
import ca.nrc.cadc.dlm.client.event.DownloadEvent;
import ca.nrc.cadc.dlm.client.event.ProgressListener;
import java.io.File;
import java.net.URL;

/**
 * TODO.
 *
 * @author pdowler
 */
public class DownloadTest
{
    public static void main(String[] args)
    {
        String baseURL = "http://scapa/getData?";
        try
        {
            Download dl = new Download();
            PL prog = new PL();
                
            dl.setDownloadListener(new ConsoleEventLogger());
            dl.setProgressListener(prog);
            
            //dl.url = new URL(baseURL + "archive=HST&file_id=U27R9G01B.2");
            //dl.label = "HST/U27R9G01B.2";
            //dl.url = new URL(baseURL + "archive=HSTCA&file_id=J8FU02030_DRZ");
            //dl.label = "HSTCA/J8FU02030_DRZ";
            //dl.url = new URL(baseURL + "archive=CFHT&file_id=535741p&cutout=[1]&compression=off");
            //dl.label = "CFHT/535741p";
            dl.url = new URL(baseURL + "archive=CFHT&file_id=686424o");
            dl.label = "CFHT/686424o";
            
            dl.decompress = false;
            dl.overwrite = true;
            dl.destDir = new File("/tmp");
            
            long t1 = System.currentTimeMillis();
            dl.run();
            long dt = System.currentTimeMillis() - t1;
            // average rate
            long rate = 1000 * (prog.totalBytes/1024) / dt;
            
            msg("duration: " + dt + " ms");
            msg("    rate: " + rate +  " KB/sec");
            msg("  output: " + dl.destFile);
            msg(" skipped: " + dl.skipped);
            msg(" failure: " + dl.failure);
            msg(" eventID: " + dl.eventID);
        }
        catch(Throwable t) { t.printStackTrace(); }
    }
    
    private static void msg(String s)
    {
         System.out.println("[DownloadTest] " + s);
    }

    private static class PL implements ProgressListener
    {
        long lastUpdate;
        int totalBytes;
        public void update(int numBytes, int totalBytes)
        {
            this.totalBytes = totalBytes;
            // try to not fire too many UI updates into the EventQueue
            long t = System.currentTimeMillis();
            long dt = t - lastUpdate;
            if (dt > 200)
            {
                lastUpdate = t;
                System.out.println("[PL] update: " + totalBytes);
            }
         }
        
         public void downloadEvent(DownloadEvent e) 
         {
             System.out.println("[PL] downloadEvent: " + e); 
         }

        public String getEventHeader()
        {
            return null;
        }
         
         
    }
}
