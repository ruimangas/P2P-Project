require 'rubygems'
require 'nokogiri' 
require 'open-uri'        
vec = []
c = 0
PAGE_URL = "http://monitor.planet-lab.eu/monitor/node"
page = Nokogiri::HTML(open(PAGE_URL))

page.css('#nodelist > tbody > tr').each do |el|
	if el.text.split(" ")[3] == "good" || el.text.split(" ")[3] == "online"
		vec << el.text.split(" ")[2]
		c = c + 1
	end
end

File.open('nodes.txt', 'w') do |f|
  vec.each do |ch|
    f.write("#{ch}\n")
  end
end

puts "#{c} nodes online."
