package br.gs.signer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/files")
public class FilesEntry {
	private static final int TIMEOUT = 10*60*1000;
	private static final Logger logger = LoggerFactory
			.getLogger(FilesEntry.class);

	@POST
	@Path("sign")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public List<String> signFiles(List<String> files) {
		try {
			logger.debug(toString(files));
			SignPanel painel = new SignPanel(files);
			int timeout = 0;
			while (!painel.getFinalized()) {
				try {
					Thread.sleep(1000);
					timeout++;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (timeout > 1000)
					return Collections.emptyList();
			}
			logger.debug(toString(painel.getFilesSigned()));
			return painel.getFilesSigned();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	@GET
	@Path("sign")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public FileList signFilesGet(@QueryParam("list") String files) {
		List<String> output = new ArrayList<String>(0);
		SignPanel painel = null;
		try {
			if (files != null && files.split("\\;").length > 0) {
				output = Arrays.asList(files.split("\\;"));
			}
			logger.debug(files);
			painel = new SignPanel(output);
			int timeout = 0;
			while (!painel.getFinalized()) {
				try {
					Thread.sleep(2000);
					timeout++;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (timeout >= TIMEOUT) {
					break;
				}
			}
			if (painel.getFinalized()) {
				logger.debug(toString(painel.getFilesSigned()));
				return gerFileList(painel.getFilesSigned());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (painel != null) {
				painel.destroy();
			}
		}
		output.clear();
		return gerFileList(output);
	}

	protected FileList gerFileList(List<String> list) {
		FileList f = new FileList();
		f.getFiles().addAll(list);
		return f;
	}

	protected Response gerResponse(List<String> list) {
		GenericEntity<List<String>> entity = new GenericEntity<List<String>>(
				list) {
		};
		return Response.ok(entity).build();
	}

	private String toString(List<String> list) {
		StringBuilder b = new StringBuilder();
		for (String s : list) {
			b.append(s);
			b.append(";");
		}
		return b.toString();
	}
}
