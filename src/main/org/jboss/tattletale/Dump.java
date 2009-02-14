/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.tattletale;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.SortedSet;
import java.util.SortedMap;
import java.util.jar.Attributes;

/**
 * Dump
 * @author Jesper Pedersen <jesper.pedersen@jboss.org>
 */
public class Dump
{
   /** New line character */
   protected static final String NEW_LINE = System.getProperty("line.separator");
   
   /**
    * Generate index.html
    * @param archives The archivess
    */
   public static void generateIndex(SortedSet<Archive> archives)
   {
      try
      {
         FileWriter fw = new FileWriter("index.html");
         BufferedWriter bw = new BufferedWriter(fw, 8192);

         bw.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">" + NEW_LINE);
         bw.write("<html>" + NEW_LINE);
         bw.write("<head>" + NEW_LINE);
         bw.write("  <title>" + Version.FULL_VERSION + ": Index</title>" + NEW_LINE);
         bw.write("  <meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">" + NEW_LINE);
         bw.write("</head>" + NEW_LINE);
         bw.write("<body>" + NEW_LINE);
         bw.write(NEW_LINE);

         bw.write("<h1>" + Version.FULL_VERSION + "</h1>" + NEW_LINE);
         
         bw.write("<h2>Dependencies</h2>" + NEW_LINE);
         bw.write("<ul>" + NEW_LINE);
         bw.write("<li><a href=\"dependencies.html\">Report</a></li>" + NEW_LINE);
         bw.write("</ul>" + NEW_LINE);

         bw.write("<h2>Multiple Jar files</h2>" + NEW_LINE);
         bw.write("<ul>" + NEW_LINE);
         bw.write("<li><a href=\"multiplejars.html\">Report</a></li>" + NEW_LINE);
         bw.write("</ul>" + NEW_LINE);

         bw.write("<h2>Multiple Locations</h2>" + NEW_LINE);
         bw.write("<ul>" + NEW_LINE);
         bw.write("<li><a href=\"multiplelocations.html\">Report</a></li>" + NEW_LINE);
         bw.write("</ul>" + NEW_LINE);

         bw.write("<h2>Jar files</h2>" + NEW_LINE);
         bw.write("<ul>" + NEW_LINE);

         for (Archive a : archives)
         {
            bw.write("<li><a href=\"" + a.getName() + ".html\">" + a.getName() + "</a></li>" + NEW_LINE);
         }

         bw.write("</ul>" + NEW_LINE);

         bw.write(NEW_LINE);
         bw.write("<p>" + NEW_LINE);
         bw.write("<hr>" + NEW_LINE);
         bw.write("Generated by: " + Version.FULL_VERSION + NEW_LINE);
         bw.write(NEW_LINE);
         bw.write("</body>" + NEW_LINE);
         bw.write("</html>" + NEW_LINE);

         bw.flush();
         bw.close();
      }
      catch (Exception e)
      {
         System.err.println("GenerateIndex: " + e.getMessage());
         e.printStackTrace(System.err);
      }
   }

   /**
    * Generate archive report
    * @param archive The archives
    */
   public static void generateArchiveReport(Archive archive)
   {
      try
      {
         FileWriter fw = new FileWriter(archive.getName() + ".html");
         BufferedWriter bw = new BufferedWriter(fw, 8192);
         bw.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">" + NEW_LINE);
         bw.write("<html>" + NEW_LINE);
         bw.write("<head>" + NEW_LINE);
         bw.write("  <title>" + Version.FULL_VERSION + ": " + archive.getName() + "</title>" + NEW_LINE);
         bw.write("  <meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">" + NEW_LINE);
         bw.write("</head>" + NEW_LINE);
         bw.write("<body>" + NEW_LINE);
         bw.write(NEW_LINE);

         bw.write("<h1>" + archive.getName() + "</h1>" + NEW_LINE);

         bw.write("<a href=\"index.html\">Main</a>" + NEW_LINE);
         bw.write("<p>" + NEW_LINE);

         bw.write("<table border=\"1\">" + NEW_LINE);
         
         bw.write("  <tr>" + NEW_LINE);
         bw.write("     <td>Name</td>" + NEW_LINE);
         bw.write("     <td>" + archive.getName() + "</td>" + NEW_LINE);
         bw.write("  </tr>" + NEW_LINE);

         bw.write("  <tr>" + NEW_LINE);
         bw.write("     <td>Locations</td>" + NEW_LINE);
         bw.write("     <td>");

         bw.write("       <table border=\"1\">" + NEW_LINE);

         Iterator<Location> lit = archive.getLocations().iterator();
         while (lit.hasNext())
         {
            Location location = lit.next();

            bw.write("      <tr>" + NEW_LINE);

            bw.write("        <td>" + location.getFilename() + "</td>" + NEW_LINE);
            bw.write("        <td>");
            if (location.getVersion() != null)
            {
               bw.write(location.getVersion());
            }
            else
            {
               bw.write("<i>Not listed</i>");
            }
            bw.write("</td>" + NEW_LINE);
            
            bw.write("      </tr>" + NEW_LINE);
         }

         bw.write("       </table>" + NEW_LINE);

         bw.write("</td>" + NEW_LINE);
         bw.write("  </tr>" + NEW_LINE);

         bw.write("  <tr>" + NEW_LINE);
         bw.write("     <td>Requires</td>" + NEW_LINE);
         bw.write("     <td>");

         Iterator<String> rit = archive.getRequires().iterator();
         while (rit.hasNext())
         {
            String require = rit.next();

            bw.write(require);

            if (rit.hasNext())
            {
               bw.write("<br>");
            }
         }

         bw.write("</td>" + NEW_LINE);
         bw.write("  </tr>" + NEW_LINE);

         bw.write("  <tr>" + NEW_LINE);
         bw.write("     <td>Provides</td>" + NEW_LINE);
         bw.write("     <td>");

         Iterator<String> pit = archive.getProvides().iterator();
         while (pit.hasNext())
         {
            String provide = pit.next();

            bw.write(provide);

            if (pit.hasNext())
            {
               bw.write("<br>");
            }
         }

         bw.write("</td>" + NEW_LINE);
         bw.write("  </tr>" + NEW_LINE);

         bw.write("</table>" + NEW_LINE);

         bw.write(NEW_LINE);
         bw.write("<p>" + NEW_LINE);
         bw.write("<hr>" + NEW_LINE);
         bw.write("Generated by: " + Version.FULL_VERSION + NEW_LINE);
         bw.write(NEW_LINE);
         bw.write("</body>" + NEW_LINE);
         bw.write("</html>" + NEW_LINE);

         bw.flush();
         bw.close();
      }
      catch (Exception e)
      {
         System.err.println("GenerateArchiveReport: " + e.getMessage());
         e.printStackTrace(System.err);
      }
   }

   /**
    * Dump dependencies
    * @param archives The archives
    * @param known Known archives
    */
   public static void generateDependencies(SortedSet<Archive> archives, Set<Archive> known)
   {
      try
      {
         FileWriter fw = new FileWriter("dependencies.html");
         BufferedWriter bw = new BufferedWriter(fw, 8192);
         bw.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">" + NEW_LINE);
         bw.write("<html>" + NEW_LINE);
         bw.write("<head>" + NEW_LINE);
         bw.write("  <title>" + Version.FULL_VERSION + ": Dependencies</title>" + NEW_LINE);
         bw.write("  <meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">" + NEW_LINE);
         bw.write("</head>" + NEW_LINE);
         bw.write("<body>" + NEW_LINE);
         bw.write(NEW_LINE);

         bw.write("<h1>Dependencies</h1>" + NEW_LINE);

         bw.write("<a href=\"index.html\">Main</a>" + NEW_LINE);
         bw.write("<p>" + NEW_LINE);

         bw.write("<table border=\"1\">" + NEW_LINE);
         
         bw.write("  <tr>" + NEW_LINE);
         bw.write("     <th>Jar file</th>" + NEW_LINE);
         bw.write("     <th>Dependencies</th>" + NEW_LINE);
         bw.write("  </tr>" + NEW_LINE);

         Iterator<Archive> it = archives.iterator();
         while (it.hasNext())
         {
            Archive archive = it.next();

            bw.write("  <tr>" + NEW_LINE);
            bw.write("     <td><a href=\"" + archive.getName() + ".html\">" + archive.getName() + "</a></td>" + NEW_LINE);
            bw.write("     <td>");

            SortedSet<String> result = new TreeSet<String>();

            Iterator<String> rit = archive.getRequires().iterator();
            while (rit.hasNext())
            {
               String require = rit.next();

               boolean found = false;
               Iterator<Archive> ait = archives.iterator();
               while (!found && ait.hasNext())
               {
                  Archive a = ait.next();

                  if (a.doesProvide(require))
                  {
                     result.add(a.getName());
                     found = true;
                  }
               }

               if (!found)
               {
                  Iterator<Archive> kit = known.iterator();
                  while (!found && kit.hasNext())
                  {
                     Archive a = kit.next();

                     if (a.doesProvide(require))
                     {
                        found = true;
                     }
                  }
               }

               if (!found)
               {
                  result.add(require);
               }
            }

            if (result.size() == 0)
            {
               bw.write("&nbsp;");
            }
            else
            {
               Iterator<String> resultIt = result.iterator();
               while (resultIt.hasNext())
               {
                  String r = resultIt.next();
                  if (r.endsWith(".jar"))
                  {
                     bw.write("<a href=\"" + r + ".html\">" + r + "</a>");
                  }
                  else
                  {
                     bw.write("<i>" + r + "</i>");                  
                  }
               
                  if (resultIt.hasNext())
                  {
                     bw.write(", ");
                  }
               }
            }

            bw.write("</td>" + NEW_LINE);
            bw.write("  </tr>" + NEW_LINE);
         }

         bw.write("</table>" + NEW_LINE);

         bw.write(NEW_LINE);
         bw.write("<p>" + NEW_LINE);
         bw.write("<hr>" + NEW_LINE);
         bw.write("Generated by: " + Version.FULL_VERSION + NEW_LINE);
         bw.write(NEW_LINE);
         bw.write("</body>" + NEW_LINE);
         bw.write("</html>" + NEW_LINE);

         bw.flush();
         bw.close();
      }
      catch (Exception e)
      {
         System.err.println("GenerateDependencies: " + e.getMessage());
         e.printStackTrace(System.err);
      }
   }

   /**
    * Dump multiple jars
    * @param gProvides The global provides map
    */
   public static void generateMultipleJars(SortedMap<String, SortedSet<String>> gProvides)
   {
      try
      {
         FileWriter fw = new FileWriter("multiplejars.html");
         BufferedWriter bw = new BufferedWriter(fw, 8192);
         bw.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">" + NEW_LINE);
         bw.write("<html>" + NEW_LINE);
         bw.write("<head>" + NEW_LINE);
         bw.write("  <title>" + Version.FULL_VERSION + ": Multiple Jar files</title>" + NEW_LINE);
         bw.write("  <meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">" + NEW_LINE);
         bw.write("</head>" + NEW_LINE);
         bw.write("<body>" + NEW_LINE);
         bw.write(NEW_LINE);

         bw.write("<h1>Multiple Jar files</h1>" + NEW_LINE);

         bw.write("<a href=\"index.html\">Main</a>" + NEW_LINE);
         bw.write("<p>" + NEW_LINE);

         bw.write("<table border=\"1\">" + NEW_LINE);

         bw.write("  <tr>" + NEW_LINE);
         bw.write("     <th>Class</th>" + NEW_LINE);
         bw.write("     <th>Jar files</th>" + NEW_LINE);
         bw.write("  </tr>" + NEW_LINE);

         Iterator it = gProvides.entrySet().iterator();
         while (it.hasNext())
         {
            Map.Entry entry = (Map.Entry)it.next();
            
            String clz = (String)entry.getKey();
            SortedSet archives = (SortedSet)entry.getValue();

            if (archives.size() > 1)
            {
               bw.write("  <tr>" + NEW_LINE);
               bw.write("     <td>" + clz + "</td>" + NEW_LINE);
               bw.write("     <td>");

               Iterator sit = archives.iterator();
               while (sit.hasNext())
               {
                  String archive = (String)sit.next();
                  bw.write("<a href=\"" + archive + ".html\">" + archive + "</a>" + NEW_LINE);

                  if (sit.hasNext())
                  {
                     bw.write(", ");
                  }
               }

               bw.write("</td>" + NEW_LINE);
               bw.write("  </tr>" + NEW_LINE);
            }
         }

         bw.write("</table>" + NEW_LINE);

         bw.write(NEW_LINE);
         bw.write("<p>" + NEW_LINE);
         bw.write("<hr>" + NEW_LINE);
         bw.write("Generated by: " + Version.FULL_VERSION + NEW_LINE);
         bw.write(NEW_LINE);
         bw.write("</body>" + NEW_LINE);
         bw.write("</html>" + NEW_LINE);

         bw.flush();
         bw.close();
      }
      catch (Exception e)
      {
         System.err.println("GenerateMultipleJars: " + e.getMessage());
         e.printStackTrace(System.err);
      }
   }

   /**
    * Dump multiple locations
    * @param archives The archives
    */
   public static void generateMultipleLocations(SortedSet<Archive> archives)
   {
      try
      {
         FileWriter fw = new FileWriter("multiplelocations.html");
         BufferedWriter bw = new BufferedWriter(fw, 8192);
         bw.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">" + NEW_LINE);
         bw.write("<html>" + NEW_LINE);
         bw.write("<head>" + NEW_LINE);
         bw.write("  <title>" + Version.FULL_VERSION + ": Multiple Locations</title>" + NEW_LINE);
         bw.write("  <meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">" + NEW_LINE);
         bw.write("</head>" + NEW_LINE);
         bw.write("<body>" + NEW_LINE);
         bw.write(NEW_LINE);

         bw.write("<h1>Multiple locations</h1>" + NEW_LINE);

         bw.write("<a href=\"index.html\">Main</a>" + NEW_LINE);
         bw.write("<p>" + NEW_LINE);

         bw.write("<table border=\"1\">" + NEW_LINE);

         bw.write("  <tr>" + NEW_LINE);
         bw.write("     <th>Name</th>" + NEW_LINE);
         bw.write("     <th>Location</th>" + NEW_LINE);
         bw.write("  </tr>" + NEW_LINE);

         for (Archive a : archives)
         {
            if (a.getLocations().size() > 1)
            {
               bw.write("  <tr>" + NEW_LINE);
               bw.write("     <td><a href=\"" + a.getName() + ".html\">" + a.getName() + "</a></td>" + NEW_LINE);
               bw.write("     <td>");

               Iterator<Location> lit = a.getLocations().iterator();
               while (lit.hasNext())
               {
                  Location location = lit.next();
                  bw.write(location.getFilename());

                  if (lit.hasNext())
                  {
                     bw.write("<br>");
                  }
               }

               bw.write("</td>" + NEW_LINE);
               bw.write("  </tr>" + NEW_LINE);
            }
         }

         bw.write("</table>" + NEW_LINE);

         bw.write(NEW_LINE);
         bw.write("<p>" + NEW_LINE);
         bw.write("<hr>" + NEW_LINE);
         bw.write("Generated by: " + Version.FULL_VERSION + NEW_LINE);
         bw.write(NEW_LINE);
         bw.write("</body>" + NEW_LINE);
         bw.write("</html>" + NEW_LINE);

         bw.flush();
         bw.close();
      }
      catch (Exception e)
      {
         System.err.println("GenerateMultipleLocations: " + e.getMessage());
         e.printStackTrace(System.err);
      }
   }
}
